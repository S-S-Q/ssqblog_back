package com.ssq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssq.constant.JwtConstant;
import com.ssq.exception.AuthenticationException;
import com.ssq.mapper.TagMapper;
import com.ssq.pojo.Blog;
import com.ssq.mapper.BlogMapper;
import com.ssq.pojo.BlogDetail;
import com.ssq.pojo.RespBean;
import com.ssq.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssq.service.ICommentService;
import com.ssq.service.ITagBlogService;
import com.ssq.service.ITagService;
import com.ssq.util.FileUtil;
import com.ssq.util.JwtTokenUtil;
import com.ssq.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Autowired
    BlogMapper blogMapper;
    @Autowired
    ITagBlogService tagBlogService;
    @Autowired
    ICommentService commentService;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${file.mdPath}")
    public String mdPath;


    //获取帖子详情页
    @Override
    public RespBean getBlogList(Integer currentPage) {
        Page page=new Page(currentPage,5);
        IPage pageData=blogMapper.selectPage(page,new QueryWrapper<Blog>().orderByDesc("created"));
        return RespBean.success("获取博客列表成功",pageData);
    }

    @Override
    public RespBean getDetailBlog(Long id, HttpServletRequest request) {
        Blog blog=blogMapper.selectById(id);
        if(blog==null)
            return RespBean.error("博客已被删除");
        if(!blog.getStatus())
        {
            String authHeader=request.getHeader(JwtConstant.TOKEN_HEADER);
            if(null==authHeader||!authHeader.startsWith(JwtConstant.TOKEN_HEAD))
            throw new AuthenticationException();
            //拿到token
            String token=authHeader.substring(JwtConstant.TOKEN_HEAD.length());
            String username= JwtTokenUtil.getUsernameFromToken(token);
            //验证token是否过期
            if(JwtTokenUtil.isTokenExpired(token))
                throw new AuthenticationException();
            //验证用户是否登出
            if(!redisUtil.hasKey(username)||!redisUtil.get(username).equals(authHeader))
                throw new AuthenticationException();
        }
        String path = mdPath + blog.getFilename();
        String str = null;
        try {
            //自定义md2Html函数转化md文件为html
            str = FileUtil.md2Html(path);
        } catch (IOException e) {
            throw new RuntimeException("出现异常");
        }
        BlogDetail blogDetail=new BlogDetail();
        blogDetail.setTitle(blog.getTitle());
        blogDetail.setCreated(blog.getCreated());
        blogDetail.setHtml(str);
        blogDetail.setTagsName(tagBlogService.getTagNamesByBlogId(blog.getId()));
        return RespBean.success("获取博客详情页成功",blogDetail);
    }

    @Override
    public RespBean editBlog(Blog blog) {
        if(blog.getId()!=null
                &&blogMapper.selectById(blog.getId())!=null)
        {
            blogMapper.updateById(blog);
            return RespBean.success("修改成功");
        }
        else
        {
            blogMapper.insert(blog);
        }
        return RespBean.success("添加成功");
    }

    @Override
    public RespBean addBlog(MultipartFile file) {
        //将md文件存储进硬盘中
        boolean save=FileUtil.saveFile(file,mdPath);
        if(!save)
            return RespBean.error("上传失败，请重新再试");
        Blog blog=null;
        Map<String,Object> blogMsgMap=null;
        try
        {
            blog=new Blog();
            blogMsgMap=FileUtil.getMsgFromFrontMatter(mdPath+'/'+file.getOriginalFilename());
            blog.setTitle((String) blogMsgMap.getOrDefault("title",file.getOriginalFilename()));
            blog.setDescription((String) blogMsgMap.getOrDefault("description",file.getOriginalFilename()));
            if(blogMsgMap.getOrDefault("date",null)!=null&&!blogMsgMap.get("date").equals(""))
                blog.setCreated(LocalDateTime.parse((String)blogMsgMap.get("date")));
            else
                blog.setCreated(LocalDateTime.now());
            blog.setFilename(file.getOriginalFilename());
            blog.setStatus(true);

        }catch (Exception e)
        {
            //如果在数据库中添加blog信息失败 则删除存储在磁盘中的md文件
            FileUtil.deleteFile(file.getOriginalFilename(),mdPath);
            throw new RuntimeException("blog添加出现错误"+e);
        }

        if(blog==null||blogMsgMap==null)
        {
            //将博客信息存储进数据库中
            blog=new Blog();
            blog.setFilename(file.getOriginalFilename());
            blog.setTitle(file.getOriginalFilename());
            blog.setDescription(file.getOriginalFilename());
            blog.setStatus(true);
            blog.setCreated(LocalDateTime.now());
        }

        //如果存在的话拿到博客id
        Blog blog_cz=blogMapper.selectOne(new QueryWrapper<Blog>().eq("title",blog.getTitle()));
        if(blog_cz==null)
        {
            blogMapper.insert(blog);
            tagBlogService.addTagBlog((List) blogMsgMap.getOrDefault("tags",null),blog.getId());
        }
        else
        {
            blogMapper.update(blog,new QueryWrapper<Blog>().eq("title",blog.getTitle()));
            //删除掉之前的tag_blog
            tagBlogService.deleteByBlogId(blog_cz.getId());
            //然后再增加tag
            tagBlogService.addTagBlog((List) blogMsgMap.getOrDefault("tags",null),blog_cz.getId());
        }
        return RespBean.success("上传成功");

    }


    @Override
    public RespBean getAllBlogList() {
        List list= blogMapper.selectList(new QueryWrapper<Blog>());
        return RespBean.success("获取成功",list);
    }

    @Override
    public RespBean getBlogsByTag(String tagName) {
        List<Long> blogIdList=tagBlogService.getBlogIdsByTag(tagName);
        List<Blog> blogs=new LinkedList<>();
        for(Long blogId :blogIdList)
        {
            Blog tmp=blogMapper.selectById(blogId);
            if (tmp!=null)
            blogs.add(tmp);
        }
        return RespBean.success("通过标签获取博客成功",blogs);
    }

    @Override
    public RespBean deleteBlogsById(List<Long> list) {
        for(Long id:list)
        {
            //需要保证文件和文件存储在数据库中信息一并删除
            Blog blog=blogMapper.selectById(id);
            boolean delete=FileUtil.deleteFile(blog.getFilename(),mdPath);
            tagBlogService.deleteByBlogId(id);
            commentService.deleteCommentByBlogId(id);
            if(delete)
                blogMapper.deleteById(id);
        }
        return RespBean.success("删除成功");
    }

    @Override
    public RespBean updateBlogStatusById(Long id,Boolean status) {
        Blog blog=blogMapper.selectById(id);
        blog.setStatus(status);
        blogMapper.updateById(blog);
        return RespBean.success("修改成功");
    }

    @Override
    public RespBean searchBlogs(String key) {
        List<Blog>blogs=new LinkedList<>();
        blogs.addAll(blogMapper.selectList(new QueryWrapper<Blog>().like("description",key).or().like("title",key)));

        return RespBean.success("获取成功",blogs);
    }
}

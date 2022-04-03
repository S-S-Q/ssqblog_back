package com.ssq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssq.config.RabbitMQConfig;
import com.ssq.config.myannotation.IdentityAuthentication;
import com.ssq.pojo.*;
import com.ssq.mapper.BlogMapper;
import com.ssq.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssq.service.ICommentService;
import com.ssq.service.ITagBlogService;
import com.ssq.util.FileUtil;
import com.ssq.util.RedisUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
    EsBlogDao esBlogDao;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

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
    @IdentityAuthentication
    public RespBean getDetailBlog(Long id, HttpServletRequest request) {
        Blog blog=blogMapper.selectById(id);
        if(blog==null)
            return RespBean.error("博客已被删除");
//        if(!blog.getStatus())
//        {
//            String authHeader=request.getHeader(JwtConstant.TOKEN_HEADER);
//            if(null==authHeader||!authHeader.startsWith(JwtConstant.TOKEN_HEAD))
//            throw new AuthenticationException();
//            //拿到token
//            String token=authHeader.substring(JwtConstant.TOKEN_HEAD.length());
//            String username= JwtTokenUtil.getUsernameFromToken(token);
//            //验证token是否过期
//            if(JwtTokenUtil.isTokenExpired(token))
//                throw new AuthenticationException();
//            //验证用户是否登出
//            if(!redisUtil.hasKey(username)||!redisUtil.get(username).equals(authHeader))
//                throw new AuthenticationException();
//        }

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
        RespBean respBean=RespBean.success("获取博客详情页成功",blogDetail);
        respBean.setIdentity(blog.getIdentity());
        return respBean;
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

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_DIRECT_INFORM, RabbitMQConfig.ROUTINGKEY_BLOG, file.getOriginalFilename());
        return RespBean.success("上传成功");

    }

    public void addBlogInEsAndMySQL(String fileName){
        Blog blog=null;
        Map<String,Object> blogMsgMap=null;
        File file=  new File(String.valueOf(Paths.get(mdPath).resolve(fileName)));
        try
        {
            blog=new Blog();
            blogMsgMap=FileUtil.getMsgFromFrontMatter(mdPath+'/'+fileName);
            blog.setTitle((String) blogMsgMap.getOrDefault("title",fileName));
            blog.setDescription((String) blogMsgMap.getOrDefault("description",fileName));
            if(blogMsgMap.getOrDefault("date",null)!=null&&!blogMsgMap.get("date").equals(""))
                blog.setCreated(LocalDateTime.parse((String)blogMsgMap.get("date")));
            else
                blog.setCreated(LocalDateTime.now());
            blog.setFilename(fileName);

        }catch (Exception e)
        {
            //如果在数据库中添加blog信息失败 则删除存储在磁盘中的md文件
//            FileUtil.deleteFile(file.getOriginalFilename(),mdPath);
            //已经更新成如果出现数据库，以及es方面的异常 那么就会反复重试
            throw new RuntimeException("blog添加出现错误"+e);
        }

        if(blog==null||blogMsgMap==null)
        {
            //将博客信息存储进数据库中
            blog=new Blog();
            blog.setFilename(fileName);
            blog.setTitle(fileName);
            blog.setDescription(fileName);
            blog.setCreated(LocalDateTime.now());
        }

        //如果存在的话拿到博客id
        Blog blog_cz=blogMapper.selectOne(new QueryWrapper<Blog>().eq("title",blog.getTitle()));
        if(blog_cz==null)
        {
            blogMapper.insert(blog);
            tagBlogService.addTagBlog((List) blogMsgMap.getOrDefault("tags",null),blog.getId());
            addOrUpdateBlogInEs(blog);
        }
        else
        {
            blogMapper.update(blog,new QueryWrapper<Blog>().eq("title",blog.getTitle()));
            //删除掉之前的tag_blog
            tagBlogService.deleteByBlogId(blog_cz.getId());
            //然后再增加tag
            tagBlogService.addTagBlog((List) blogMsgMap.getOrDefault("tags",null),blog_cz.getId());
            addOrUpdateBlogInEs(blog_cz);
        }
    }


    private void addOrUpdateBlogInEs(Blog blog){
        EsBlog esBlog=new EsBlog();
        esBlog.setTitle(blog.getTitle());
        esBlog.setCreated(blog.getCreated());
        esBlog.setId(blog.getId());
        System.out.println(blog.getId());
        esBlog.setDescription(blog.getDescription());
        try {
            esBlog.setContent(FileUtil.md2Html(mdPath + blog.getFilename()));
        }catch (IOException e)
        {
            System.out.println("文件转换出现问题");
        }
        esBlogDao.save(esBlog);
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
            //删除ES中的备份
            deleteEsBlog(id);
            if(delete)
                blogMapper.deleteById(id);
        }
        return RespBean.success("删除成功");
    }

    private void deleteEsBlog(Long blogId)
    {
        EsBlog esBlog=new EsBlog();
        esBlog.setId(blogId);
        esBlogDao.delete(esBlog);
    }

    @Override
    public RespBean updateBlogStatusById(Long id,Integer status) {
        Blog blog=blogMapper.selectById(id);
        blog.setStatus(status);
        blogMapper.updateById(blog);
        return RespBean.success("修改成功");
    }

    @Override
    public RespBean searchBlogs(String key) {
        List<EsBlog>esBlogs=highLightEsSearch(key);

        return RespBean.success("获取成功",esBlogs);
    }


    public List<EsBlog> highLightEsSearch(String key)
    {
        //多条件查询
        //只要满足一个条件就可以
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title",key))
                .should(QueryBuilders.matchQuery("description",key))
                .should(QueryBuilders.matchQuery("content",key));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightFields(
                        new HighlightBuilder.Field("title")
                        ,new HighlightBuilder.Field("description")
                ,new HighlightBuilder.Field("content"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>"))
                .build();


        SearchHits<EsBlog> search = elasticsearchRestTemplate.search(searchQuery, EsBlog.class);
        List<SearchHit<EsBlog>> searchHits = search.getSearchHits();
        List<EsBlog>esBlogs=new LinkedList<>();
        for(SearchHit<EsBlog> searchHit:searchHits){
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();


            //将高亮的内容填充到content中
            searchHit.getContent().setDescription(highlightFields.get("description")==null ? searchHit.getContent().getDescription():highlightFields.get("description").get(0));
            searchHit.getContent().setTitle(highlightFields.get("title")==null ? searchHit.getContent().getTitle():highlightFields.get("title").get(0));
            searchHit.getContent().setContent(highlightFields.get("content")==null ? searchHit.getContent().getContent():highlightFields.get("content").get(0));


            //如果是用户可见的才放到实体类中
            Blog blog=blogMapper.selectById(searchHit.getContent().getId());
            esBlogs.add(searchHit.getContent());
        }
        return esBlogs;
    }
}

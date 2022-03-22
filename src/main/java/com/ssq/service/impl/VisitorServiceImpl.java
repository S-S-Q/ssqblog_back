package com.ssq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssq.pojo.RespBean;
import com.ssq.pojo.Visitor;
import com.ssq.mapper.VisitorMapper;
import com.ssq.pojo.VisitorStatics;
import com.ssq.service.IVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SSQ
 * @since 2022-02-24
 */
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements IVisitorService {


    @Autowired
    VisitorMapper visitorMapper;

    @Override
    public void addVisitor(Visitor visitor) {
        int count=visitorMapper.selectCount(new QueryWrapper<Visitor>().eq("visit_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        if(count==0)
        visitorMapper.insert(visitor);
    }

    @Override
    public RespBean getVisitor() {
        List<Visitor>visitors=visitorMapper.selectList(new QueryWrapper<Visitor>().orderByDesc("visit_time"));
        return RespBean.success("获取成功",visitors);
    }

    @Override
    public RespBean getStaticsMsg() {
        List<VisitorStatics>visitorStatics=visitorMapper.getIPGroup();
        return RespBean.success("获取成功",visitorStatics);
    }
}

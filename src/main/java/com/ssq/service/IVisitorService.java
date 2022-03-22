package com.ssq.service;

import com.ssq.pojo.RespBean;
import com.ssq.pojo.Visitor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SSQ
 * @since 2022-02-24
 */
public interface IVisitorService extends IService<Visitor> {

    void addVisitor(Visitor visitor);


    RespBean getVisitor();

    RespBean getStaticsMsg();
}

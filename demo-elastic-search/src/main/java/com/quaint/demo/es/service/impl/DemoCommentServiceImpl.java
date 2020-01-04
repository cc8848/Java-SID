package com.quaint.demo.es.service.impl;

import com.quaint.demo.es.search.handler.abst.IndexDataHandler;
import com.quaint.demo.es.service.DemoCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author quaint
 * @date 2020-01-04 14:08
 */
@Service
public class DemoCommentServiceImpl implements DemoCommentService {

    @Autowired
    List<IndexDataHandler> indexDataHandlerList;


    @Override
    public void refreshAll() {
        indexDataHandlerList.forEach(IndexDataHandler::refresh);
    }
}

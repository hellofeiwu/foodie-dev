package com.imooc.controller;

import com.imooc.service.ItemsESService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("items")
public class ItemsController {
    public static final Integer PAGE_SIZE = 20;

    @Autowired
    private ItemsESService itemsESService;

    @GetMapping("/hello")
    public Object hello() {
        return "Hello Elasticsearch~";
    }

    @GetMapping("es/search")
    public IMOOCJSONResult search(
                                String keywords,
                                String sort,
                                Integer page,
                                Integer pageSize) {
        if(keywords == null) {
            return IMOOCJSONResult.errorMsg(null);
        }
        sort = sort == null ? "" : sort;
        page = page == null ? 1 : page;
        pageSize = pageSize == null ? PAGE_SIZE : pageSize;

        page--; // es中分页是从0开始的

        PagedGridResult result = itemsESService.searchItems(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(result);
    }
}

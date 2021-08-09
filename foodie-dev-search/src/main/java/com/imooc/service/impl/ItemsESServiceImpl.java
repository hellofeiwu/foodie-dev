package com.imooc.service.impl;

import com.imooc.es.pojo.Items;
import com.imooc.es.pojo.Stu;
import com.imooc.service.ItemsESService;
import com.imooc.utils.PagedGridResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemsESServiceImpl implements ItemsESService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        String fieldName = "itemName";

        SortBuilder sortBuilder = null;
        if (sort.equals("c")) {
            sortBuilder = new FieldSortBuilder("sellCounts")
                    .order(SortOrder.DESC);
        } else if (sort.equals("p")) {
            sortBuilder = new FieldSortBuilder("price")
                    .order(SortOrder.ASC);
        } else {
            sortBuilder = new FieldSortBuilder("itemName.keyword") // type 为text的field要用其下keyword作为排序条件
                    .order(SortOrder.ASC);
        }

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(fieldName, keywords))
                .withHighlightFields(new HighlightBuilder.Field(fieldName)
                        .preTags("<font color=red>").postTags("</font>"))
                .withPageable(pageable)
                .withSort(sortBuilder)
                .build();
        AggregatedPage<Items> pagedItems = esTemplate.queryForPage(query, Items.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<Items> itemsList = new ArrayList<>();

                SearchHits hits = searchResponse.getHits();
                for (SearchHit h : hits) {
                    HighlightField highlightField = h.getHighlightFields().get(fieldName);
                    String itemName = highlightField.getFragments()[0].toString();

                    String itemId = (String)h.getSourceAsMap().get("itemId");
                    String imgUrl = (String)h.getSourceAsMap().get("imgUrl");
                    Integer price = (Integer)h.getSourceAsMap().get("price");
                    Integer sellCounts = (Integer)h.getSourceAsMap().get("sellCounts");

                    Items item = new Items();
                    item.setItemId(itemId);
                    item.setItemName(itemName);
                    item.setImgUrl(imgUrl);
                    item.setPrice(price);
                    item.setSellCounts(sellCounts);

                    itemsList.add(item);
                }

                return new AggregatedPageImpl<>((List<T>) itemsList, pageable, searchResponse.getHits().totalHits);
            }
        });
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(pagedItems.getContent());
        gridResult.setPage(page++); // es中分页是从0开始的
        gridResult.setTotal(pagedItems.getTotalPages());
        gridResult.setRecords(pagedItems.getTotalElements());

        return gridResult;
    }
}

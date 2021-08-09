package com.test;

import com.imooc.Application;
import com.imooc.es.pojo.Items;
import com.imooc.es.pojo.Stu;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {
    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Test
    public void createIndexStu() {
        Stu stu = new Stu();
        stu.setStuId(1006L);
        stu.setName("iron man");
        stu.setAge(40);
        stu.setMoney(199.8f);
        stu.setSign("I am iron man");
        stu.setDescription("I have a iron army");

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();

        esTemplate.index(indexQuery);
    }

    @Test
    public void createIndexItem() {
        int count = 1002;
        while (count < 1010) {
            Items items = new Items();
            items.setItemId(String.valueOf(count));
            items.setItemName("item name");
            items.setImgUrl("item url");
            items.setPrice(500);
            items.setSellCounts(5);

            IndexQuery indexQuery = new IndexQueryBuilder().withObject(items).build();

            esTemplate.index(indexQuery);
            count ++;
        }
    }

    @Test
    public void searchStuDoc() {
        Pageable pageable = PageRequest.of(0, 10);

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "a"))
                .withPageable(pageable)
                .build();
        AggregatedPage<Stu> pagedStu = esTemplate.queryForPage(query, Stu.class);
        System.out.println("检索后的总分页数为：" + pagedStu.getTotalPages());
        List<Stu> stuList = pagedStu.getContent();
        for (Stu s : stuList) {
            System.out.println(s);
        }
    }

    @Test
    public void searchStuDocHighlight() {
        Pageable pageable = PageRequest.of(0, 10);

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "a"))
                .withHighlightFields(new HighlightBuilder.Field("description")
                .preTags("<font color=red>").postTags("</font>"))
                .withPageable(pageable)
                .build();
        AggregatedPage<Stu> pagedStu = esTemplate.queryForPage(query, Stu.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<Stu> stuList = new ArrayList<>();

                SearchHits hits = searchResponse.getHits();
                for (SearchHit h : hits) {
                    HighlightField highlightField = h.getHighlightFields().get("description");
                    String description = highlightField.getFragments()[0].toString();

                    Stu stu = new Stu();
                    stu.setDescription(description);

                    stu.setStuId(Long.valueOf(h.getSourceAsMap().get("stuId").toString()));
                    stu.setName((String) h.getSourceAsMap().get("name"));
                    stu.setAge((Integer) h.getSourceAsMap().get("age"));
                    stu.setSign((String) h.getSourceAsMap().get("sign"));
                    stu.setMoney(Float.valueOf(h.getSourceAsMap().get("money").toString()));

                    stuList.add(stu);
                }

                if (stuList.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) stuList);
                }
                return null;
            }
        });
        System.out.println("检索后的总分页数为：" + pagedStu.getTotalPages());
        List<Stu> stuList = pagedStu.getContent();
        for (Stu s : stuList) {
            System.out.println(s);
        }
    }

    @Test
    public void searchStuDocHighlightSort() {
        Pageable pageable = PageRequest.of(0, 10);

        SortBuilder sortBuilder = new FieldSortBuilder("money")
                .order(SortOrder.ASC);
        SortBuilder sortBuilderAge = new FieldSortBuilder("age")
                .order(SortOrder.DESC);

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "a"))
                .withHighlightFields(new HighlightBuilder.Field("description")
                        .preTags("<font color=red>").postTags("</font>"))
                .withSort(sortBuilder)
                .withSort(sortBuilderAge)
                .withPageable(pageable)
                .build();
        AggregatedPage<Stu> pagedStu = esTemplate.queryForPage(query, Stu.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<Stu> stuList = new ArrayList<>();

                SearchHits hits = searchResponse.getHits();
                for (SearchHit h : hits) {
                    HighlightField highlightField = h.getHighlightFields().get("description");
                    String description = highlightField.getFragments()[0].toString();

                    Stu stu = new Stu();
                    stu.setDescription(description);

                    stu.setStuId(Long.valueOf(h.getSourceAsMap().get("stuId").toString()));
                    stu.setName((String) h.getSourceAsMap().get("name"));
                    stu.setAge((Integer) h.getSourceAsMap().get("age"));
                    stu.setSign((String) h.getSourceAsMap().get("sign"));
                    stu.setMoney(Float.valueOf(h.getSourceAsMap().get("money").toString()));

                    stuList.add(stu);
                }

                if (stuList.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) stuList);
                }
                return null;
            }
        });
        System.out.println("检索后的总分页数为：" + pagedStu.getTotalPages());
        List<Stu> stuList = pagedStu.getContent();
        for (Stu s : stuList) {
            System.out.println(s);
        }
    }
}

package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.utils.PagedGridResult;
import io.swagger.models.auth.In;

import java.util.List;

public interface ItemService {
    /**
     * 根据商品 id 查询详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品 id 查询商品图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品 id 查询商品规格
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品 id 查询商品参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品 id 查询商品的评价等级数量
     * @param itemId
     */
    public CommentLevelCountsVO queryItemCommentCounts(String itemId);

    /**
     * 根据商品id查询商品的评价（分页）
     * @param itemId
     * @param level
     * @return
     */
    public PagedGridResult queryPagedComments(
            String itemId,
            Integer level,
            Integer page,
            Integer pageSize
    );
}

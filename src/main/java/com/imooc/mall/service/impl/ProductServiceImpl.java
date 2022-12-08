package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：商品Service实现类;
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

    /**
     * 新增商品
     * @param addProductReq
     */
    @Override
    public void add(AddProductReq addProductReq) {
        //因为，我们是通过Product这个类，和数据库打交道的;
        //所以，我们把addProductReq的内容，复制成一个product;
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);

        //首先，我们要看下，商品时候重名
        Product productOld = productMapper.selectByName(product.getName());
        //如果查出来了，表示数据库中已经有叫这个名字的商品了；那么我们就抛出一个名字已存在的异常
        if (productOld != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        //如果上面没问题，就去调用方法，向数据库中插入数据；
        int count = productMapper.insertSelective(product);
        //如果count=0，表示插入失败了；我们就抛出一个新增失败的异常；
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }
    }

    /**
     * 更新商品
     * @param product
     */
    @Override
    public void update(Product product) {
        //先根据商品名，去数据库中查，看是否有叫这个名字的商品数据
        Product productOld = productMapper.selectByName(product.getName());
        //如果上面查到了
        //而且【上面查到的商品的id】和【我们前台穿过来的商品id不一样】，那么不允许更新，抛出不允许重名异常；
        // 很显然，这一条也是为了防止商品名重名；
        if (productOld != null && productOld.getId() != product.getId()) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        //然后调用Dao层的更新方法，去更新数据
        int count = productMapper.updateByPrimaryKeySelective(product);
        //如果更新失败，就抛出更新失败异常；
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     * 删除商品
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //先查查，看表里是否有这条数据
        Product productOld = productMapper.selectByPrimaryKey(id);
        //如果查不到数据，就抛出删除失败异常；（很容理解，都查不到这条数据，当然无法删除了）
        if (productOld == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_ERROR);
        }
        //如果一切OK，就调用【mybatis-generator帮我们生成的】删除方法去删除；
        int count = productMapper.deleteByPrimaryKey(id);
        //如果没有删除成功，就抛出删除失败异常；
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_ERROR);
        }
    }

    /**
     * 批量上下架商品
     * @param ids
     * @param sellStatus
     */
    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    /**
     * 后台的，获取商品的列表
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        //设置分页的：当前页，每一页的记录数；
        PageHelper.startPage(pageNum, pageSize);
        //调用Dao层的方法，去查询
        List<Product> productList = productMapper.selectListForAdmin();
        //得到PageInfo对象
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    /**
     * 根据id，查询商品
     * @param id
     * @return
     */
    @Override
    public Product detail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    /**
     * 根据条件，以分页的方式，查询商品数据；
     * @param productListReq
     * @return
     */
    @Override
    public PageInfo list(ProductListReq productListReq) {
        //先构建一个专门助力于查询的Query对象
        ProductListQuery productListQuery = new ProductListQuery();

        //搜索条件处理
        //如果前端传了keyword这个参数；（其实，也就是前端搜索关键词了）：就把这个条件赋值到productListQuery查询对象上去；
        if (!StringUtils.isEmpty(productListReq.getKeyword())) {
            //根据keyword拼凑"%keyword%",以好在数据库中进行模糊查询;
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            //然后，把这个在查询数据库时，可以直接使用的"%keyword%"查询条件，赋值到productListQuery查询对象上去；
            productListQuery.setKeyword(keyword);
        }
        //如果前端传了categoryId这个参数；（其实，也就是前端选择某个商品目录）：就把这个条件赋值到productListQuery查询对象上去；
        if (productListReq.getCategoryId() != null) {
            //目录处理：如果查询某个目录下的商品，不仅要查询隶属于该目录下的商品，也要查询隶属于该目录的子目录下的商品；
            //所以，先调用以前编写的一个【递归查询所有子目录】的方法；获取当前目录和当前目录的所有子目录的递归查询结果；
            List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            //但是，上面的categoryVOList是一个嵌套的结果，我们要想获取当前目录和其子目录的CategoryId的集合，还需要做以下处理；
            //首先，创建一个List用来存放所有的CategoryId
            List<Integer> categoryIds = new ArrayList<>();
            //很自然，当前传参的这个CategoryId肯定在需要添加到集合中；
            categoryIds.add(productListReq.getCategoryId());
            //编写一个【遍历categoryVOList这种递归结构的数据，以获取所有categoryId的，工具方法】
            getCategoryIds(categoryVOList, categoryIds);
            //然后，把这个在查询数据库时，【当前目录和当前目录所有子目录的categoryIds】的查询条件，赋值到productListQuery查询对象上去；
            productListQuery.setCategoryIds(categoryIds);
        }

        //排序条件的处理
        //首先，尝试从productListReq这个传递参数中，获取排序的参数
        String orderBy = productListReq.getOrderBy();
        //如果我们从前端请求中的参数中，有orderBy这个有关排序的参数；；如果这个有关排序的参数，在我们预设的排序条件中的话；
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            //如果前端没有传orderBy参数，或者，传递orderBy参数的值不符合我们在【Constant.ProductListOrderBy.PRICE_ASC_DESC】中定义的格式
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }


        //调用Dao层编写的(可能有条件的)查询语句
        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    /**
     * 工具方法，遍历【List<CategoryVO> categoryVOList】这种递归嵌套的数据结构，获取其中所有的categoryId;
     * @param categoryVOList
     * @param categoryIds
     */
    private void getCategoryIds(List<CategoryVO> categoryVOList, List<Integer> categoryIds) {
        //遍历传过来的这个【递归嵌套接口的，CategoryVOList】
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO =  categoryVOList.get(i);
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());

                //递归调用
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }

        }
    }
}
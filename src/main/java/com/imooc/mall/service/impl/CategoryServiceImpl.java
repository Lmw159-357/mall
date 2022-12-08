package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import springfox.documentation.annotations.Cacheable;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：分类目录CategoryService接口的实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 增加目录分类
     * @param addCategoryReq
     */
    @Override
    public void add(AddCategoryReq addCategoryReq) {
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq, category);
        //根据目录名去查询，数据库中是否已有这个名字的目录；
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        //如果查到了，说明【我们要增加了这个目录的，目录名】在数据库中已经有叫这个名字的目录了；抛出【不允许重名的业务异常】
        if (categoryOld != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        //调用方法，插入数据
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {//如果count=0，表示插入没有成功；则抛出【新增失败】的业务异常
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }
    }

    /**
     * 更新目录分类
     * @param updateCategory
     */
    @Override
    public void update(Category updateCategory) {
        //因为分类的名字不允许重复；所以，在更新前需要有下面的判断逻辑；
        if (updateCategory.getName() != null) {
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
            if (categoryOld.getName() != null && !categoryOld.getId().equals(updateCategory.getId())) {
                throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
            }
        }
        //如果没有name重名的风险，就调用方法去更新；
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     * 删除目录分类
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //先根据id，尝试去查询有没有这个分类
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        //如果根据id，没有查到记录；那说明数据库没有这个id的记录；那么就抛出删除失败的异常
        if (categoryOld == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_ERROR);
        }
        //如果根据id，查到了记录；那么就调用删除方法去删除
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {//如果返回值为0，就表示删除失败了；抛出删除失败的异常
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_ERROR);
        }
    }

    /**
     * 查询所有的目录分类数据，并包装成PageInfo分页对象；
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        //设置分页的：当前页，每一页的记录数；
        // 然后，查询结果先按照type排序（从小到大排序）；如果type相同，就按照order_num排序；
        PageHelper.startPage(pageNum, pageSize, "type,order_num");
        //调用Dao层的方法，去查询
        List<Category> categoryList = categoryMapper.selectList();
        //得到PageInfo对象
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    /**
     * 递归查询得到，分类目录数据；（针对前台的）
     * @return
     */
    @Override
    @Cacheable(value = "listCategoryForCustomer")
    public List<CategoryVO> listCategoryForCustomer(Integer parentId) {
        //定义一个List，这个List就用来存在最终的查询结果；即，这个List中的直接元素是：所有的parent_id=0，即type=1的，第1级别的目录；
        List<CategoryVO> categoryVOList = new ArrayList<CategoryVO>();

        //我们额外创建recursivelyFindCategories()方法，去实现递归查询的逻辑；
        //我们第一次递归查询时，是先查一级目录；（而一级目录的parentId是0）
        //该方法第一个参数是：List<CategoryVO> categoryVOList：用来存放当前级别对应的，所有的下一级目录数据；
        //  PS：对于【最终返回给前端的List<CategoryVO> categoryVOList】来说，其所谓的下一级目录就是：所有的parent_id=0，即type=1的，
        //  第1级别的目录；
        //  PS：对于【所有的parent_id=0，即type=1的，第1级别的目录；】来说，其categoryVOList就是【List<CategoryVO> childCategory属性】，其是用来存放该级别对应的所有的parent_id=1，即type=2的，第2级别的目录；
        //  PS：对于【所有的parent_id=1，即type=2的，第2级别的目录；】来说，其categoryVOList就是【List<CategoryVO> childCategory属性】，其是用来存放该级别对应的所有的parent_id=2，即type=3的，第3级别的目录；
        //该方法的第二个参数是：当前级别目录的parent_id，即也就是当前级别的上一级目录的id；
        //即，第一个参数是【上一级别的List<CategoryVO> categoryVOList】；第二参数是【下一级别的parent_id，也就是当前级别的id】；
        recursivelyFindCategories(categoryVOList, parentId);
        return categoryVOList;
    }


    /**
     * 递归查询分类目录数据的，具体逻辑；；；其实就是，递归获取所有目录分类和子目录分类，并组合称为一个“目录树”；
     * @param categoryVOList ：存放所有下级别分类目录的数据；
     * @param parentId ：某级分类目录的parentId；
     */
    private void recursivelyFindCategories(List<CategoryVO> categoryVOList, Integer parentId) {
        //首先，根据parent_id，查询出所有该级别的数据；（比如，第一次我们查询的是parent_id=0，即type=1的，第1级别的目录）
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        //然后，遍历上面查询的该级别的数据；去尝试查询该级别数据的，下一级别的数据；
        if (!CollectionUtils.isEmpty(categoryList)) {
            //遍历所有查到的当前级别数据，把其放在对应上级目录的【List<CategoryVO> categoryVOList】中；
            for (int i = 0; i < categoryList.size(); i++) {
                //获取到【上面查询的，该级别数据中的，一条数据】，把其存储到上级目录的List<CategoryVO> childCategory属性中；
                //自然，如果该级别是【parent_id=0，即type=1的，第1级别的目录】，就是把其存储在最顶级的、返回给前端的那个List<CategoryVO> categoryVOS中；
                Category category =  categoryList.get(i);
                CategoryVO categoryVo = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVo);
                categoryVOList.add(categoryVo);

                //然后，这一步是关键：针对【每一个当前级别的，目录数据】去递归调用recursivelyFindCategories()方法；
                //自然，第一个参数是【当前级别数据的，List<CategoryVO> childCategory属性】：这是存放所有下级别目录数据的；
                //第二个参数是【当前级别数据的id】：这自然是下级别目录数据的parent_id:
                recursivelyFindCategories(categoryVo.getChildCategory(), categoryVo.getId());
            }
        }
    }
}
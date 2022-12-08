package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * 描述：【商品模块】后台的Controller
 */
@Controller
public class ProductAdminController {

    @Autowired
    ProductService productService;

    @ApiOperation("新增商品")
    @PostMapping("/admin/product/add")
    @ResponseBody
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    /**
     * 上传文件（这儿具体来说，就是图片）
     * @param httpServletRequest
     * @param file
     * @return
     */
    @ApiOperation("上传文件（这儿具体来说，就是图片）")
    @PostMapping("/admin/upload/file")
    @ResponseBody
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {

        //获取文件的原始名字
        String fileName = file.getOriginalFilename();
        //通过截取最后一个“.”后面的内容，获取文件扩展名
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        //利用UUID，生成文件上传到服务器中的文件名；
        UUID uuid = UUID.randomUUID();//通过Java提供的UUID工具类，获取一个UUID；
        //把uuid和文件扩展名，拼凑成新的文件名；
        String newFileName = uuid.toString() + suffix;

        //生成文件夹的File对象；
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        //生成文件的File对象；
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        //如果文件夹不存在的话
        if (!fileDirectory.exists()) {
            //如果在创建这个文件夹时，创建失败，就抛出文件夹创建失败异常
            if (!fileDirectory.mkdir()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.MKDIR_FAILED);
            }
        }
        //如果能执行到这儿，说明文件夹已经创建成功了；；；那么就把传过来的文件，写入到我们指定的File对象指定的位置中去；
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //执行到这儿以后，表示，我们已经把文件，存放到指定的位置了；
        //接下来，就是组织图片的url，返回给前端；
        try {
//            System.out.println(httpServletRequest.getRequestURL() + "");
//            System.out.println(getHost(new URI(httpServletRequest.getRequestURL() + "")));
            return ApiRestResponse.success(
                    getHost(new URI(httpServletRequest.getRequestURL() + "")) +
                            "/images/" + newFileName);
        } catch (URISyntaxException e) {
            //如果上面的过程出现了问题，就抛出文件上传失败异常；
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPLOAD_FAILED);
        }
    }

    /**
     * 工具方法：获取图片完整地址中的，URI：
     * 即通过【"http://127.0.0.1:8080/images/bfe5d66d-98b1-4825-9a86-de8c0741328a.png"】得到【"http://127.0.0.1:8080/"】
     * @param uri
     * @return
     */
    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            //
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
                    uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    /**
     * 更新商品
     * @param updateProductReq
     * @return
     */
    @ApiOperation("更新商品")
    @PostMapping("/admin/product/update")
    @ResponseBody
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    /**
     * 后台删除商品
     * @param id
     * @return
     */
    @ApiOperation("删除商品")
    @PostMapping("/admin/product/delete")
    @ResponseBody
    public ApiRestResponse deleteProduct(@RequestParam("id") Integer id) {
        productService.delete(id);
        return ApiRestResponse.success();
    }

    /**
     * 后台批量上下架商品
     *
     * @return
     */
    @ApiOperation("批量上下架商品")
    @PostMapping("/admin/product/batchUpdateSellStatus")
    @ResponseBody
    public ApiRestResponse batchUpdateSellStatus(@RequestParam("ids") Integer[] ids, @RequestParam("sellStatus") Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台的商品列表")
    @GetMapping("/admin/product/list")
    @ResponseBody
    public ApiRestResponse list(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }
}
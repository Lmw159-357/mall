package com.imooc.mall.exception;

import com.imooc.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：  处理统一异常的handler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 最后的兜底方案：处理【剩余的，没有被其他方法处理的】+【Exception异常，或者继承Exception的异常】；
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("Default Exception",e);
        return ApiRestResponse.error(ImoocMallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 处理自定义ImoocMallException自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(ImoocMallException.class)
    @ResponseBody
    public Object handleImoocMallException(ImoocMallException e) {
        log.error("ImoocMallException",e);
        return ApiRestResponse.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理@Valid所引发的，参数校验失败引发的【MethodArgumentNotValidException】异常；
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException",e);
        return handleBindingResult(e.getBindingResult());

    }

    /**
     * 处理【MethodArgumentNotValidException】异常；提取错误信息，构建ApiRestResponse统一返回对象；
     * @param result
     * @return
     */
    private ApiRestResponse handleBindingResult(BindingResult result) {
        //把【MethodArgumentNotValidException异常】处理为，对应的ApiRestResponse统一返回对象；
        //这儿创建一个List集合；后面我们在MethodArgumentNotValidException中获取的错误信息，都存放在这个集合中去；
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {//如果BindingResult中，包含错误，就获取其中所有的错误信息；
            List<ObjectError> allErrors = result.getAllErrors();
            //遍历所有的错误信息；
            for (int i = 0; i < allErrors.size(); i++) {
                ObjectError objectError =  allErrors.get(i);
                //提取具体的错误信息；
                String message = objectError.getDefaultMessage();
                //将错误信息，添加到list集合中
                list.add(message);
            }
        }
        if (list.size() == 0) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        //根据MethodArgumentNotValidException异常的具体错误信息，构建ApiRestResponse统一返回对象；
        return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),list.toString());
    }
}
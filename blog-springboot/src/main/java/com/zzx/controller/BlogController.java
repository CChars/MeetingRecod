package com.zzx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzx.config.RedisConfig;
import com.zzx.model.entity.PageResult;
import com.zzx.model.entity.Result;
import com.zzx.model.entity.StatusCode;
import com.zzx.model.pojo.Blog;
import com.zzx.service.BlogService;

import com.zzx.utils.FormatUtil;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Api(tags = "博文api", description = "博文api", basePath = "/blog")
@RestController
@RequestMapping("/blog")
public class BlogController {

    private static final String IMAGE_JPG = ".jpg";

    private static final String IMAGE_PNG = ".png";

    @Autowired
    private BlogService blogService;

    @Autowired
    private FormatUtil formatUtil;

    @ApiOperation(value = "上传文件", notes = "文件")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) {
        if (!formatUtil.checkObjectNull(file)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        String fileFormat = formatUtil.getFileFormat(file.getOriginalFilename());

        if (null == fileFormat) {
            return Result.create(StatusCode.ERROR, "文件缺少格式");
        }
        //仅仅在只校验图片时使用
//        if (!IMAGE_JPG.equals(fileFormat.toLowerCase()) && !IMAGE_PNG.equals(fileFormat.toLowerCase())) {
//            return Result.create(StatusCode.ERROR, "文件格式错误");
//        }
        try {
            String url = blogService.saveFile(file);
            return Result.create(StatusCode.OK, "上传成功", url);
        } catch (IOException ioe) {
            return Result.create(StatusCode.ERROR, "上传失败" + ioe.getMessage());
        }
    }


    /**
     * 保存博文，博文内容由前端md编辑器生成
     *
     * @param blogBody
     * @param blogTitle
     * @return
     */
    @ApiOperation(value = "发布博文", notes = "博文标题+博文内容+博文标签")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
//    public Result newBlog(String blogTitle, String blogBody, Integer[] tagId) {
    public Result newBlog(@RequestBody Map<String, Object> params) {
//        if (!formatUtil.checkStringNull(blogTitle, blogBody) || !formatUtil.checkPositive(tagId)) {
        ArrayList<Integer> p = (ArrayList<Integer>) params.get("tagId");
        Integer[] tagId = p.toArray(new Integer[p.size()]);
//        String jsonObject= (String) params.get("blog"); // 将数据转成json字符串
        LinkedHashMap linkedHashMap = (LinkedHashMap) params.get("blog");

        String json = JSON.toJSONString(linkedHashMap);
        Blog blog = JSON.parseObject(json, Blog.class); //将json转成需要的对象
//        Blog blog = (Blog) params.get("blog");
        if (!formatUtil.checkPositive(tagId)) {
            return Result.create(StatusCode.ERROR, "请补全会议内容");
        }
        try {
            blogService.saveBlog(blog, tagId);
            return Result.create(StatusCode.OK, "发布会议记录成功");
        } catch (IOException e) {
            return Result.create(StatusCode.ERROR, "非法操作");
        }
    }


    @ApiOperation(value = "根据id查询博文", notes = "博文id")
    @GetMapping("/{blogId}/{isHistory}")
    public Result findBlogById(@PathVariable Integer blogId, @PathVariable boolean isHistory) {
        if (!formatUtil.checkObjectNull(blogId, isHistory)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        try {
            return Result.create(StatusCode.OK, "查询成功", blogService.findBlogById(blogId, isHistory));
        } catch (RuntimeException e) {
            return Result.create(StatusCode.NOTFOUND, "此博客不存在");
        } catch (IOException e) {
            return Result.create(StatusCode.ERROR, "此博客不存在");
        }

    }

    /**
     * 根据用户分页查询博文
     *
     * @param page      页数
     * @param showCount 显示条数
     * @return
     */
    @ApiOperation(value = "根据用户分页查询会议", notes = "页数+显示数量")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/myblog/{page}/{showCount}")
    public Result findBlogByUser(@PathVariable Integer page, @PathVariable Integer showCount) {

        if (!formatUtil.checkPositive(page, showCount)) {
            return Result.create(StatusCode.OK, "参数错误");
        }

        PageResult<Blog> pageResult =
                new PageResult<>(blogService.getBlogCountByUser(), blogService.findBlogByUser(page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }


    /**
     * 首页分页查询
     * 查询的范围在 最近10条博客 内
     *
     * @param page      页码
     * @param showCount 显示条数
     * @return
     */
    @ApiOperation(value = "首页分页查询会议", notes = "页数+显示数量")
    @GetMapping("/home/{page}/{showCount}/{tagId}")
    public Result homeBlog(@PathVariable Integer page, @PathVariable Integer showCount, @PathVariable Integer tagId) {
        if (!formatUtil.checkPositive(page, showCount) || showCount > RedisConfig.REDIS_NEW_BLOG_COUNT) {
            return Result.create(StatusCode.OK, "参数错误");
        }


        try {
            PageResult<Blog> pageResult = null;
            if (tagId == 0) {
                pageResult = new PageResult<>(blogService.getHomeBlogCount(), blogService.findHomeBlog(page, showCount));
            } else {
                pageResult = new PageResult<>(blogService.getHomeBlogCountByTag(tagId), blogService.findHomeBlogByTag(page, showCount,tagId));
            }
            return Result.create(StatusCode.OK, "查询成功", pageResult);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.create(StatusCode.SERVICEERROR, "服务异常");
        }


    }

    /**
     * 热门博文
     * 正常状态
     *
     * @return
     */
    @ApiOperation(value = "首页会议", notes = "首页会议")
    @GetMapping("/hotBlog")
    public Result hotBlog() {

        try {
            return Result.create(StatusCode.OK, "查询成功", blogService.findHotBlog());
        } catch (IOException e) {
            return Result.create(StatusCode.SERVICEERROR, "服务异常");
        }
    }


    /**
     * 博文搜索
     * 正常状态
     *
     * @param search
     * @param page
     * @param showCount
     * @return
     */
    @ApiOperation(value = "分页搜索会议", notes = "搜索内容+页码+显示条数")
    @GetMapping("/searchBlog/{page}/{showCount}")
    public Result searchBlog(String search,
                             @PathVariable Integer page,
                             @PathVariable Integer showCount) {
        if (!formatUtil.checkPositive(page, showCount) || showCount > RedisConfig.REDIS_NEW_BLOG_COUNT) {
            return Result.create(StatusCode.OK, "参数错误");
        }

        if (!formatUtil.checkStringNull(search)) {
            return Result.create(StatusCode.OK, "参数错误");
        }

        PageResult<Blog> pageResult = new PageResult<>(blogService.getSearchBlogCount(search),
                blogService.searchBlog(search, page, showCount));
        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }


    /**
     * 查询所有博客，包括删除状态
     *
     * @return
     */
    @ApiOperation(value = "管理员查询会议", notes = "管理员查询会议")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/AllBlog/{page}/{showCount}")
    public Result findAllBlog(@PathVariable Integer page, @PathVariable Integer showCount) {
        if (!formatUtil.checkPositive(page, showCount)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        PageResult<Blog> pageResult = new PageResult<>(blogService.getAllBlogCount(), blogService.findAllBlog(page, showCount));

        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }

    @ApiOperation(value = "修改会议", notes = "会议id+会议+会议标签")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{blogId}")
//    public Result updateBlog(@PathVariable Integer blogId, String blogTitle, String blogBody, Integer[] tagId) {
    public Result updateBlog(@PathVariable Integer blogId, @RequestBody Map<String, Object> params) {

//        if (!formatUtil.checkStringNull(blogTitle, blogBody)) {
//            return Result.create(StatusCode.ERROR, "参数错误");
//        }
        ArrayList<Integer> p = (ArrayList<Integer>) params.get("tagId");
        Integer[] tagId = p.toArray(new Integer[p.size()]);
//        String jsonObject= (String) params.get("blog"); // 将数据转成json字符串
        LinkedHashMap linkedHashMap = (LinkedHashMap) params.get("blog");

        String json = JSON.toJSONString(linkedHashMap);
        Blog blog = JSON.parseObject(json, Blog.class); //将json转成需要的对象

        if (!formatUtil.checkPositive(tagId) || !formatUtil.checkPositive(blogId)) {
            return Result.create(StatusCode.ERROR, "请补全会议内容");
        }

        try {
            blog.setId(blogId);
//            blogService.updateBlog(blogId, blogTitle, blogBody, tagId);
            blogService.updateBlog(blogId, blog, tagId);
            return Result.create(StatusCode.OK, "修改成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "修改失败" + e.getMessage());
        } catch (JsonProcessingException e) {
            return Result.create(StatusCode.SERVICEERROR, "服务异常");
        }
    }


    @ApiOperation(value = "用户删除博文", notes = "博文id")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{blogId}")
    public Result deleteBlog(@PathVariable Integer blogId) {
        if (!formatUtil.checkPositive(blogId)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }

        try {
            blogService.deleteBlog(blogId);
            return Result.create(StatusCode.OK, "删除成功");
        } catch (RuntimeException e) {
            return Result.create(StatusCode.ERROR, "删除失败" + e.getMessage());
        } catch (JsonProcessingException e) {
            return Result.create(StatusCode.SERVICEERROR, "服务异常");
        }
    }

    @ApiOperation(value = "管理员删除博文", notes = "博文id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/{blogId}")
    public Result adminDeleteBlog(@PathVariable Integer blogId) throws JsonProcessingException {
        if (!formatUtil.checkPositive(blogId)) {
            return Result.create(StatusCode.ERROR, "参数错误");
        }
        blogService.adminDeleteBlog(blogId);
        return Result.create(StatusCode.OK, "删除成功");
    }


    @ApiOperation(value = "管理员分页搜索博文", notes = "搜索内容+页码+显示条数")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/searchAllBlog/{page}/{showCount}")
    public Result searchAllBlog(String search,
                                @PathVariable Integer page,
                                @PathVariable Integer showCount) {

        if (!formatUtil.checkPositive(page, showCount)) {
            return Result.create(StatusCode.OK, "参数错误");
        }
        if (!formatUtil.checkStringNull(search)) {
            return Result.create(StatusCode.OK, "参数错误");
        }

        PageResult<Blog> pageResult = new PageResult<>(blogService.getSearchAllBlogCount(search),
                blogService.searchAllBlog(search, page, showCount));


        return Result.create(StatusCode.OK, "查询成功", pageResult);
    }


    @ApiOperation(value = "按月份归档博客", notes = "按月份归档博客")
    @GetMapping("/statisticalBlogByMonth")
    public Result statisticalBlogByMonth() {
        try {
            return Result.create(StatusCode.OK, "查询成功", blogService.statisticalBlogByMonth());
        } catch (IOException e) {
            return Result.create(StatusCode.SERVICEERROR, "服务异常");
        }
    }

    /**
     * @Description:
     * @Param: [blogId]
     * @return: com.zzx.model.entity.Result
     * @Author: Tyson
     * @Date: 2020/5/30/0030 12:55
     */
    @ApiOperation(value = "获取博客点赞数")
    @GetMapping("/getBlogLikeCount/{blogId}")
    public Result getBlogLikeCount(@PathVariable Integer blogId) {
        try {
            int likeCount = blogService.getBlogLikeCountByBlogId(blogId);
            return Result.create(StatusCode.OK, "获取点赞数成功", likeCount);
        } catch (RuntimeException re) {
            return Result.create(StatusCode.ERROR, re.getMessage());
        }
    }

//    @ApiOperation(value = "用户上传文件", notes = "文件")
//    @PreAuthorize("hasAuthority('USER')")
//    @PostMapping("/upload")
//    public Result uploadFile(@PathVariable MultipartFile file) {
//
//    }

}

<template>
  <div>
    <el-container>
      <el-main style="width: 75%">
        <indexBlogList :tagID="tag"></indexBlogList>
      </el-main>
      <el-aside id="aside" style="width: 20%" class="hidden-xs-only">
        <div style="margin: 10px">
          <introduction />
          <!-- <newDiscuss/> -->
          <!-- <hotBlog/> -->
          <statisticalBlog />
          <myTagShow @getBlogTag="setTagToList($event)"></myTagShow>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>
<script>
import introduction from "@/components/introduction";
import newDiscuss from "@/components/newDiscuss";
import hotBlog from "@/components/hotBlog";
import statisticalBlog from "@/components/statisticalBlog";
import indexBlogList from "@/components/indexBlogList";
import myTagShow from "@/components/myTagShow";

import "element-ui/lib/theme-chalk/display.css";

export default {
  name: "index",
  components: {
    introduction,
    newDiscuss,
    hotBlog,
    statisticalBlog,
    indexBlogList,
    myTagShow,
  },
  data() {
    return {
      tag: ""
    };
  },
  methods: {
    setTagToList(tagID) {
      this.tag = tagID;
      console.log(this.tag);
    }
  }
};
</script>
<style scoped>
</style>

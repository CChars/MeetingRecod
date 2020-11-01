<template>
  <el-card id="myTag">
    <p>
      <span style="color:#67C23A" class="el-icon-collection-tag">会议标签</span>
    </p>
    <hr/>

    <!-- <div v-for="tag in tags" :key="tag.id"> -->
      <el-tag v-for="tag in tags" :key="tag.id" type="success"  style="margin-top: 10px; margin-left: 20px;" @click="searchThisBranchListWithtag(tag.id)">
        <el-link type="success" :underline="false">{{tag.name}}</el-link>
      </el-tag>
    <!-- </div> -->
  </el-card>
</template>

<script>
  import tag from '@/api/tag'

  export default {
    name: 'myTag',
    data() {
      return {
        tags: []
      }
    },
    created() {
      this.load()
    },
    methods: {
      load() {
        tag.getAllTag().then(res => {
          this.tags = res.data;
        })
      },

      searchThisBranchListWithtag(tagId) {
          //点击的时候调用父表单里面的加载方法，并传入标签的id 来筛选全部对应id的会议
          this.$emit("getBlogTag", tagId);
      }
    }
  }
</script>
<style scoped>
  #myTag {
    /*-moz-box-shadow: 0px 6px 0px #333333;*/
    /*-webkit-box-shadow: 0px 6px 0px #333333;*/
    /*box-shadow: 0px 3px 10px #333333;*/
    margin: 30px 10px;

  }
</style>

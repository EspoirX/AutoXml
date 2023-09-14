# AutoXml
backgroundlibrary 的简化版，主要是提供圆角边框等用得最频繁的功能

## 使用



## 代码提示
将项目中的 AutoXml.xml 放入：  

mac as4.0 之后目录进行了修改:
/Users/xxx/Library/Application Support/Google/AndroidStudio4.1/templates
windows:
进入目录C:\Users\XXX\.AndroidStudio3.2\config\templates

没有文件夹就创建一个

初始化：

在 ActivityLifecycleCallbacks 的 onActivityCreated 中调用 AutoXmlManager.inject(activity)

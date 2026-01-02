## zglj-gyjh: TODO20pt, Do the right thing, YiDuiLaJi Not waste Time ##
- doc : all documents knowledge base
- each demo project has its own doc folder if it's really its demo specific
- typical demo project structure:
  quick-poc-mpt
    - doc -- api.http and prompt.txt
    - deploy -- docker k8s build script
    - sre/main/java
    - src/main/resources/script -- dev/sit/uat/prod and other scripts folder 
    - src/main/resources/ignore -- setting.conf files

#### Priority to put doc & script:
#### 1. [demo script](springboot-jpa-demo%2Fsrc%2Fmain%2Fresources%2Fscript)
#### 2. [demo doc](springboot-jpa-demo%2Fdoc)
#### 3. [general doc](doc)

### copy-tool use springboot-jpa-demo to copy-create new project.
[copy-tool](copy-tool)
copy-tool/src/main/java/ps/demo/copy/CopyTool.java -- copy create from springboot-jpa-demo [springboot-jpa-demo](springboot-jpa-demo) 
copy-tool/src/main/java/ps/demo/copy/CopyToolMpt.java -- copy create from quick-poc-mpt [quick-poc-mpt](quick-poc-mpt)

## simple-demo Useful tools (Refer to simple-demo project):
src/main/java/pslab/Kuaima.java -- auto CRUD
src/main/java/pslab/NativeHookDemo.java -- UI record key & mouse and run
src/main/java/pslab/Main.java -- act as command listening application
src/main/java/ps/demo/util/SwingTool.java -- UI open chrome with webdriver and commands
src/main/java/ps/demo/util/MyRobotUtil.java -- Robot auto click and paste
src/main/java/ps/demo/util/MyImageUtil.java -- image tool, screenshot, comparing etc.


# 简易版spring

## 功能
1. 会在启动的时候将所有controller下面的接口全部获取出来
2. 通过字典树构建成一个链表，在用户请求的时候可以通过handlerMapping来获取对应的方法
3. 然后通过handlerAdaptor来获取参数和执行对应的方法
4. 只需要和springboot一样声明方法接口，然后dispatchServlet里面可以将对应的参数传递到接口方法参数里面。
5. 一个简单版ioc容器，可以保存所有bean和加载配置文件
6. 在context管理的类里面，可以通过@YhValue注入 配置文件的配置
7. 可以扫描所有@bean注解，并将其返回值注册到容器中
8. 统一配置所有的请求和响应编码

## todo
1. 目前只能接收json格式数据和返回json格式数据
2. 添加接收文件的数据


## 配置
通过反射获取方法参数名字，
需要在javac 添加 -parameters




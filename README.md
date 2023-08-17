### k8s webhook搭建开发环境

#### 搭建go开发环境
+ 下载go版本大于1.16
+ 执行go env 确保 GO111MODULE=on
+ 设置GOPROXY=https://goproxy.io,direct


#### 搭建minikube 
+ 下载。。。安装   
+ 启动后台服务： minikube start --image-mirror-country cn --iso-url=https://kubernetes.oss-cn-hangzhou.aliyuncs.com/minikube/iso/minikube-v1.5.0.iso --registry-mirror=https://hcbnbv4g.mirror.aliyuncs.com --kubernetes-version=v1.16.0
+ 启动ui
  + minikube dashboard --url & 
  + kubectl proxy --address=0.0.0.0 --accept-hosts='.*' --port 20908 &
  + 访问地址：http://10.213.33.119:20908/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/#/deployment/default/testnode?namespace=default
+ 注册和安装ngrok
+ 启动ngrok: ./ngrok http 443 获取到外网访问地址
+ vim ./deployment/kubectl/mutatingwebhook-template.yaml： 
  ```  
    clientConfig:
    url:  https://d83d-113-116-40-240.ngrok.io/mutate
  ```
  中url为ngrok公网地址
+ 给要切入的namespace打标签，对default namespace 打标签： 执行 ```kubectl label namespace default agent-operator-injector=enabled```
+ 本地调试启动main.go, minikube ui上创建一个deployment, 我们采用 ```springio/gs-spring-boot-docker```镜像来创建一个spring应用
+ 结束，正常情况k8s 会回调到本地断点，完成调试

#### 测试拦截dubbo请求
```
    dubbo-provider image:
    registry.eptok.com/yspay-test/mock/provider
    
    dubbo-consumer image:
    registry.eptok.com/yspay-test/mock/consumer
    
```

部署consumer 并且创建service
```
kubectl expose deployment/consumer --type="NodePort" --port 8080
export NODE_PORT=$(kubectl get services/consumer -o go-template='{{(index .spec.ports 0).nodePort}}')
curl http://$(minikube ip):$NODE_PORT/sayHello?name=xxx

```


### mock agent 实现逻辑
+ 采用bytebuddy 切入类：```com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker``` 
+ 代理切入后的逻辑到 ```DubboInterceptor intercept```方法
+ 请求dubbo mock config 服务器判断接口是否启用了mock数据 如果配置正确则返回mock数据，否则执行原有逻辑
+ 采用maven shade plugin，定义premain 主类名称




 









 




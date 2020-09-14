# spring-cloud-function-demo-gradle

AWS SAM と SpringCloudFunction を利用した Lambda デプロイのデモアプリケーション（Gradle 編）

- Maven との違い
  - Mavenはビルド結果をjarとして実行可能だが、Gradleはzipにしないと実行できない
  - それに引きづられて、`sam build` ではなく、`gradlew build` を使う
  - このため、template.yaml 内の CodeUri の指定を、ビルド後のzipファイルにしておく

## ビルド／デプロイ コマンド

```bash
### ビルドする
### gradleの場合、`sam build` で生成される.aws-sam/配下がうまく読めないため、ビルドはgradleコマンドを使う
### build/distributions/ 配下にzipファイルが生成される
$ ./gradlew build

### ローカルで実行する（event.json を入力値として作動する）
$ sam local invoke -t template.yaml -e events/event.json

### ローカルでAPI Gatewayモックを起動する
$ sam local start-api -t template.yaml

### パッケージングしてS3にzipをアップロードする
$ sam package\
 -t ${TEMPLATE_FILENAME}\
 --output-template-file package.yml\
 --s3-bucket ${S3_BUCKET_NAME}\
 --region ap-northeast-1\
 --force-upload

### デプロイする
$ sam deploy\
 -t package.yml\
 --stack-name ${STACK_NAME}\
 --capabilities CAPABILITY_IAM\
 --region ap-northeast-1\
 --role-arn ${SAM_EXEC_ROLE_ARN}\
 --force-upload\
 --parameter-overrides Env=${ENV}
```

## sam publish

`sam package` まで実行して `sam deploy` を実行せずに `sam publish` を実行する

```bash
### S3のパスを指定する必要があるため、sam package 後の package.yml を指定するのが最適
$ sam publish -t package.yml
```

## samconfig.toml

samconfig.toml を用意することで、`sam deploy` のみでデプロイ可能となる
（sam package コマンドを省略して、sam deploy もオプション省略できる）

```toml
version = 0.1
[default]
[default.deploy]
[default.deploy.parameters]
stack_name = "CloudFormationのスタック名"
s3_bucket = "パッケージをアップロードするS3バケット名"
s3_prefix = "S3のプレフィックス（あれば）"
region = "ap-northeast-1"
confirm_changeset = false
capabilities = "CAPABILITY_IAM"
parameter_overrides = "PARAM1=VAL Env=dev DeployType=all"
```

## 後始末

- 作成したCloudFormationスタックの削除
- ロググループの削除（スタックと連動させていない or DeletionPolicy がRetain の場合は個別で削除が必要）

```bash
### CloudFormationスタックの削除コマンド
$ aws cloudformation delete-stack --stack-name api-policy-stack 
```
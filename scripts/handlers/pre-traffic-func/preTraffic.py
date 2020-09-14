import os
import json
import boto3

CODE_DEPLOY = boto3.client('codedeploy')
AWS_LAMBDA = boto3.client('lambda')

def notify_execution_status(event, status):
    deployment_id = event.get('DeploymentId')
    execution_id = event.get('LifecycleEventHookExecutionId')
    return CODE_DEPLOY.put_lifecycle_event_hook_execution_status(deploymentId=deployment_id,
                                                                 lifecycleEventHookExecutionId=execution_id,
                                                                 status=status)

def lambda_handler(event, context):
    function_name = os.getenv('FUNCTION_NAME')
    callback = os.getenv('IS_CALLBACK')
    status = 'Failed'

    try:
        input_event = {"body": "{\"test\":\"testvalue\"}"}
        res = AWS_LAMBDA.invoke(FunctionName=function_name
                                ,InvocationType='RequestResponse'
                                ,Payload=json.dumps(input_event)
                                )

        # Lambdaの実行結果を取得
        payload = json.loads(res['Payload'].read())
        res_status = payload["statusCode"]

        # lambdaの実行結果判定 TODO: 判定条件は要検討
        assert res_status == 200

    except Exception as e:
        # assert が false なら'Failed'で応答
        status = 'Failed'
        print('assert Failed.')
    else:
        # assert が true なら'Succeeded'で応答
        status = 'Succeeded'
        print('assert Succeeded.')

    if callback == 'true':
        print('do callback status: ' + status)
        notify_response = notify_execution_status(event, status)
        print(notify_response)
    else:
        print('nothing callback. status: ' + status)


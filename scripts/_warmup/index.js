'use strict';

const aws = require('aws-sdk');
aws.config.region = 'ap-northeast-1';
const lambda = new aws.Lambda();
const functions = [{"config": {"enabled": true}}];

module.exports.warmUp = async (event, context) => {
    console.log('Warm Up Start');

    const invokes = await Promise.all(functions.map(async (func) => {
        let targetFunctionName;
        targetFunctionName = process.env.TARGET_FUNCTION_NAME;
        let concurrency;
        concurrency = parseInt(process.env.WARMUP_CONCURRENCY);
        console.log(`Warming up function: ${targetFunctionName} with concurrency: ${concurrency}`);

        const clientContext = "{\"source\":\"serverless-plugin-warmup\"}";
        const payload = "{\"body\":\"{\\\"test\\\":\\\"value\\\"}\"}";
        const params = {
            ClientContext: Buffer.from(`{"custom":${clientContext}}`).toString('base64'),
            FunctionName: targetFunctionName,
            InvocationType: 'RequestResponse',
            LogType: 'None',
            Qualifier: process.env.TARGET_ALIAS,
            Payload: payload
        };

        try {
            await Promise.all(Array(concurrency).fill(0).map(async () => await lambda.invoke(params).promise()));
            console.log(`Warm Up Invoke Success: ${targetFunctionName}`);
            return true;
        } catch (e) {
            console.log(`Warm Up Invoke Error: ${targetFunctionName}`, e);
            return false;
        }
    }));

    console.log(`Warm Up Finished with ${invokes.filter(r => !r).length} invoke errors`);
}


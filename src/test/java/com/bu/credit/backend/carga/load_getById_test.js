import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

export let options = {
  stages: [
    { duration: '30s', target: 10 },
    { duration: '1m', target: 50 },
    { duration: '2m', target: 100 },
    { duration: '30s', target: 0 },
  ],
};

export default function () {
  let res = http.get('http://localhost:8080/api/v1/debt/1');

  check(res, {
    'status é 200': (r) => r.status === 200,
    'resposta não está vazia': (r) => r.body.length > 0,
  });

  errorRate.add(res.status !== 200);

  sleep(1);
}

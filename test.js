import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

let errorRate = new Rate('errors');

export let options = {
  scenarios: {
    postAndThenGet: {
      executor: 'shared-iterations',
      vus: 200,
      iterations: 200,
      exec: 'postAndThenGetScenario',
    },
  },
  setupTimeout: '2m',
};

export function handleSummary(data) {
  return {
    "/reports/apiReports.html": htmlReport(data),
  };
}

export function setup() {
  sleep(20);
}

export function postAndThenGetScenario() {
  postRequest();
  getRequest();
}

function postRequest() {
  let url = 'http://app:8080/api/v1/debt';
  let payload = JSON.stringify({
    totalAmount: 69,
    creditorName: "Fernando da Silva",
    dueDate: "2024-05-31",
    numberOfInstallments: 3
  });

  let params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  let postRes = http.post(url, payload, params);

  check(postRes, {
    'status é 200': (r) => r.status === 200,
    'resposta não está vazia': (r) => r.body && r.body.length > 0,
  });

  errorRate.add(postRes.status !== 200);

  sleep(1);
}

function getRequest() {
  let getRes = http.get('http://app:8080/api/v1/debt/1');

  check(getRes, {
    'status é 200': (r) => r.status === 200,
    'resposta não está vazia': (r) => r.body && r.body.length > 0,
  });

  errorRate.add(getRes.status !== 200);

  sleep(1);
}

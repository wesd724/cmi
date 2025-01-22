import requests
from sklearn.svm import SVR
from sklearn.ensemble import RandomForestRegressor
from datetime import datetime, timedelta
from flask import Flask, jsonify
from flask_cors import CORS

import time

app = Flask(__name__)
CORS(app)

@app.route('/predict')
def getRank():
    return jsonify(rankingComparePrevPrice())


def fetchData(market, count, to = ""): # 주가 데이터 가져오기
    url = f"https://api.upbit.com/v1/candles/days"
    params = {"market": market, "count": count, "to": to }
    response = requests.get(url, params=params)
    data = response.json()
    return data


def predictNextPrice(market, days): # 주가 예측 함수
    res = fetchData(market, days, datetime.now().replace(microsecond=0) - timedelta(0)) # 데이터 가져오기
    prev = res[0]['opening_price']
    
    t = len(res)
    data = [i['opening_price'] for i in res][::-1]  # [200일 전,... , 오늘]로 정렬
    X = [[i] for i in range(1, t + 1)]  # 입력 데이터: 일 수
    
    l = len(str(round(prev))) + 1

    model = RandomForestRegressor(n_estimators=50, max_features='sqrt') # RandomForest 모델 학습
    #model = SVR(kernel='rbf', C=10 ** l, gamma=0.001) # SVR 모델 학습
    model.fit(X, data)
    
    prediction = model.predict([[t + 1]]) # 다음 날 주가 예측
    return (prev, prediction[0])


def rankingComparePrevPrice():
    markets = [
        "KRW-BTC",
        "KRW-ETH",
        "KRW-SOL",
        "KRW-XRP",
        "KRW-BCH",
        "KRW-AAVE",
        "KRW-ETC",
        "KRW-NEO",
        "KRW-XLM",
        "KRW-STX",
        "KRW-DOGE",
        "KRW-EGLD",
        "KRW-ARK",
        "KRW-LINK",
        "KRW-AVAX",
        "KRW-QTUM",
        "KRW-ZETA",
        "KRW-SUI",
        "KRW-MASK",
        "KRW-NEAR"
    ]
    
    days = 200

    predictions = []
    # 주가 예측
    for market in markets:
        #for gamma in [0.001, 0.002, 0.005, 0.007, 0.01, 0.02, 0.05, 0.1, 0.2]:
            prev, pred = predictNextPrice(market, days)
            comparePrev = round((pred - prev) / prev * 100, 2)
            print(f"{market}: 오늘 가격: {format(prev, ',')}, 내일 예측 가격: {format(pred, ',')}, 전일 대비: {comparePrev}")
            predictions.append({"market": market, "comparedPreviousDay": comparePrev})
            time.sleep(0.1)
    return sorted(predictions, key=lambda x: -x["comparedPreviousDay"])


if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True)
    
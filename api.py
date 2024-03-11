from websocket import WebSocketApp   # websocket-client
from flask import Flask
from flask_cors import CORS
app = Flask(__name__)
CORS(app)

@app.route('/api')
def api():
  ws_app.run_forever()
  return 'test'

def on_message(ws, message):
    data = message.decode('utf-8')
    print(data)

def on_error(ws, err):
    print(err)


def on_close(ws, status_code, msg):
    print("closed!")


def on_connect(ws):
    print("connected!")
    # Request after connection
    ws.send('''
    [
        {
            "ticket": "test example"
        },
        {
            "type": "trade",
            "codes": [
            "KRW-BTC"
            ]
        },
        {
            "format": "DEFAULT"
        }
    ]   
    ''')


ws_app = WebSocketApp("wss://api.upbit.com/websocket/v1",
                                on_message=on_message,
                                on_open=on_connect,
                                on_error=on_error,
                                on_close=on_close)
#

if __name__ == '__main__':
    app.run()
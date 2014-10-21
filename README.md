# 入力プログラム実行＆テスト

## 注意

* 不本意なプログラムが実行されてしまう可能性があるという脆弱性があるので、身内のみで公開する等、注意してください。  
（今後対応予定）
* 無限ループするコード等入力されると落ちるかもしれません。（timeoutとかまだ設定していない）

## 使用可能言語

* Java


## 問題配置方法

root にディレクトリを作ると、その名前の問題が追加されます。  
問題のディレクトリを question_dir と呼ぶことにします。


### 問題に入力が無い場合

question_dir に output.txt というファイルを設置します。  
output がこれと一致すれば Accepted となります。


### 問題に入力がある場合

question_drt に テストケースとして

* input0.txt, input1.txt, input2.txt, ...
* output0.txt, output1.txt, output2.txt, ...

を設置します。

input0.txt を入力した結果が output0.txt と一致... というように調べていき、すべて一致したら Accepted となります。  
間違えたテストケースがある場合、そこで停止します。  
間違えたテストケースの入力と正解出力、実際の出力を提示しまう。


## TODO

* 脆弱性への対応
* 複数言語への対応
* 入出力への対応
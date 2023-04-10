# usingChatGPT
이 프로젝트는 ChatGPT를 활용하여 코드 푸시 시 자동으로 코드 리뷰를 받을 수 있는 기능을 구현한 깃헙 액션입니다. 자바 코드에 대한 리뷰를 제공하며, 코드 품질, 모범 사례, 가독성 및 유지 관리 관점에서 평가해 줍니다.

## 사용법
본인의 깃헙 리포지토리에 .github/workflows 폴더를 생성하고, code_review.yml 파일을 추가합니다. 아래 내용을 그대로 복사 붙여넣기 해도 됩니다.
```yaml```
```
name: Code Review

on:
  push:
    paths:
      - '**.java'

jobs:
  code_review:
    runs-on: ubuntu-latest

    steps:
      - name: Check out repository
        uses: actions/checkout@v3
        with:
          fetch-depth: 0

      - name: Set up Python
        uses: actions/setup-python@v3
        with:
          python-version: '3.9'

      - name: Install dependencies
        run: |
          python -m pip install --upgrade pip
          pip install gitpython
          pip install openai
        

      - name: Run code review
        env:
          OPENAI_API_KEY: ${{ secrets.OPENAI_API_KEY }}
        run: python code_review.py
        ```
        
리포지토리에 code_review.py 파일을 생성하고 아래 코드를 복사 붙여넣기 합니다.

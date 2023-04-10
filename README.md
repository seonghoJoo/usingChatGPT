#usingChatGPT
이 프로젝트는 ChatGPT를 활용하여 코드 푸시 시 자동으로 코드 리뷰를 받을 수 있는 기능을 구현한 깃헙 액션입니다. 자바 코드에 대한 리뷰를 제공하며, 코드 품질, 모범 사례, 가독성 및 유지 관리 관점에서 평가해 줍니다.

##사용법
본인의 깃헙 리포지토리에 .github/workflows 폴더를 생성하고, code_review.yml 파일을 추가합니다. 아래 내용을 그대로 복사 붙여넣기 해도 됩니다.
yaml
```Copy code
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
```
python
Copy code
import os
import subprocess
import openai

# Set your OpenAI API key
openai.api_key = os.environ["OPENAI_API_KEY"]

try:
    output = subprocess.check_output(["git", "diff", "--diff-filter=M" ,"--name-only", "HEAD~3", "HEAD"]).decode("utf-8")
except subprocess.CalledProcessError:
    print("exception 발생")
    output = subprocess.check_output(["git", "show", "--pretty=format:", "--name-only", "HEAD~3" ,"HEAD"]).decode("utf-8")
print(output)
files = output.splitlines()
# Filter Java files
java_files = [file for file in files if file.endswith(".java")]
# Review each Java file
for index, file in enumerate(java_files, start=1):
    print(f"Code Review {index} of {len(java_files)}\n")
    print(f"File: {file}\n")
    print("=" * 80)

    # Read the file content
    with open(file, "r") as f:
        code = f.read()

    # Request a code review from ChatGPT
    prompt = f"Please review the following Java code:\n{code}\n, focusing on code quality, best practices, readability, and maintainability. Provide specific suggestions for improvement, including any potential optimizations, refactoring opportunities, or areas where I could improve the code's design and structure. Additionally, please point out any possible bugs or performance issues that you notice."

    response = openai.Completion.create(
        engine="text-davinci-003",
        prompt=prompt,
        max_tokens=150,
        n=1,
        stop=None,
        temperature=0.5,
    )

    # Print the code review
    print(response.choices[0].text
    ```

import openai
import os
import subprocess
import re

openai.api_key = os.getenv("OPENAI_API_KEY")

# Get the list of changed files
output = subprocess.check_output(["git", "diff", "--name-only", "HEAD^", "HEAD"])
file_paths = output.decode("utf-8").split("\n")

# Filter the list to include only .java files
java_files = [path for path in file_paths if path.endswith(".java")]

# Read the content of each .java file
code_files = []
for file_path in java_files:
    with open(file_path, "r") as file:
        code_files.append({"path": file_path, "content": file.read()})

# Generate code reviews for each .java file
for code_file in code_files:
    prompt = f"Please review the following Java code in '{code_file['path']}':\n\n{code_file['content']}\n\nAnd provide feedback on clean code, run-time exceptions, recommended values, and method names."

    response = openai.Completion.create(
        engine="text-davinci-002",  # You can replace this with the GPT-4 engine when it becomes available
        prompt=prompt,
        max_tokens=150,
        n=1,
        stop=None,
        temperature=0.5,
    )

    print(f"Code review for {code_file['path']}:\n{response.choices[0].text.strip()}\n")
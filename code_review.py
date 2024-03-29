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
    print(response.choices[0].text.strip())
    print("\n" + "-" * 80 + "\n")

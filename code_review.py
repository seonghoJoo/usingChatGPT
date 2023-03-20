import os
import subprocess
import openai

# Set your OpenAI API key
openai.api_key = os.environ["OPENAI_API_KEY"]


# Fetch the remote branch
subprocess.run(["git", "fetch", "origin", "main"])

# Get the commit hash of the last remote commit
remote_commit = subprocess.check_output(["git", "rev-parse", "origin/master"]).decode("utf-8").strip()

# Get the commit hash of the current local commit
local_commit = subprocess.check_output(["git", "rev-parse", "HEAD"]).decode("utf-8").strip()

# Get the list of modified files between the remote and the current local commit
output = subprocess.check_output(["git", "log", "--name-only", "--pretty=format:", f"{remote_commit}..{local_commit}"]).decode("utf-8")
print(output)
files = output.splitlines()
# Filter Java files
java_files = [file for file in files if file.endswith(".java")]

# Review each Java file
for file in java_files:
    print(f"Code review for {file}:")

    # Read the file content
    with open(file, "r") as f:
        code = f.read()

    # Request a code review from ChatGPT
    prompt = f"Please provide a code review for the following Java code:\n\n```java\n{code}\n\n And provide feedback on clean code, run-time exceptions, recommended values, and method names. I need a senior java spring boot programmer. I need you advice me like senior (10+ years) programmer```"

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

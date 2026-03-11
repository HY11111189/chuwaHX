
# Chuwa Training

## How to submit your assignments using this repo

### Step 1: Fork this repo
Students **must fork** this repository to their own GitHub account first.

Click **Fork** on the top-right corner of this repo.

---

### Step 2: Clone *your forked repo*
Open your terminal and run:

```bash
cd your_work_dir
git clone https://github.com/<your_github_username>/chuwa3926.git
cd chuwa3926
```

(Optional but recommended) Add the original repo as upstream:
```bash
git remote add upstream https://github.com/KKKTT-cyk/chuwa3926.git
```

## Step 3: Create your own master branch

Create a personal master branch in your fork:
```bash
git checkout -b firstName_lastName/master
git push origin firstName_lastName/master
```

## Step 4: Create a homework feature branch

For each homework, create a new branch from your own master branch:
```bash
git checkout firstName_lastName/master
git checkout -b firstName_lastName/hw1
```
Work on your assignment, then commit and push:
```bash
git add .
git commit -m "Finish HW1"
git push origin firstName_lastName/hw1
```

## Step 5: Raise a Pull Request (PR)

Go to your fork on GitHub.

Create a Pull Request:


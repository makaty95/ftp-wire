We welcome contributors to **ftp-wire** project, if you would like to fix some existing issue or even report a new one.

Ways you can contribute to this project:
- Update docs files, more clear instructions or diagrams
- Bug fixes
- Add Unit/Integration tests
- New features implementation (you should open a new issue discussing it and asking for a feedback before starting)

> If you are new to Github open source contribution, check [Contributing to a project](https://docs.github.com/en/get-started/exploring-projects-on-github/contributing-to-a-project).

Everyone is welcomed contributing to this project, Things to consider while your are working on this project:
- The project uses Java jdk-17
- We use [Jline](https://jline.org/docs/intro) to customize client and server CLIs
- You should add unit tests for any feature or bug fixes which you implement, feel free to ask about this in the PR
- Please try to write a clean commented code, this will help any maintainer to check and review your code.

## Unit/Integration tests
It's recommended to add tests regularly as you implement your code,

Run all tests via mavan:
```bash
mvn test
```

## Project docs
This project uses [Material for MK-Docs](https://squidfunk.github.io/mkdocs-material/) to create documentation.
All docs files is located in the `/docs` directory.

### Publishing
We use Github [pages with Github Actions](https://squidfunk.github.io/mkdocs-material/publishing-your-site/#with-github-actions) to publish the docs every push to the `master` branch.

Check `.github/workflows/` directory for more info. 

downloader
==========
Designed a download manager to manage one user’s download tasks; for each URI provided by the user, the download manager launches multiple threads to download different parts of the task simultaneously to accelerate the download process. It also provides a GUI to give its user fine control of the download process. For example, the user can create, pause, resume and cancel a download task. Moreover, the user can assign different level of priorities to download tasks, and the scheduler of the download manager will adjust the downloading speed of each task according to its current priority.

Compiled application is in dist directory, type java -jar Downloader.jar to run.

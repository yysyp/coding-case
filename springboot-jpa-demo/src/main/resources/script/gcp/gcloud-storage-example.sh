mkdir aaa1
touch aaa1/test1.txt

gcloud storage cp --recursive ./aaa1 gs://folder1/

==>>result:
folder1/aaa1/test1.txt


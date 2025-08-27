#! /bin/bash
set -eu

cat <<EOF > policy.json
{
  "xxx": "xxx"
}
EOF

export PROJECT_ID=xxxxx
gcloud secrets create xxx --replication-policy-file policy.json --project "$PROJECT_ID"

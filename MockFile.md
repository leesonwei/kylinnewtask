671118655
projectList:
{
  "status": 0,
  "msg": "SUCCESS",
  "data": [
    {
      "uuid": "cf8376eb-15a9-21b3-b8dd-9818c46a0bc6",
      "version": "3.0.0.20500",
      "name": "NearRealTime",
      "status": "ENABLED",
      "tables": [
        "DEFAULT.NEARREALTIMETABLE",
        "DEFAULT.V_F_MO_STATS"
      ],
      "realizations": [
        {
          "type": "CUBE",
          "realization": "NearRealTime_cube"
        }
      ],
      "models": [
        "NearRealTime_model"
      ],
      "description": ""
    },
    {
      "uuid": "739bcb2f-f1c7-bfa9-630a-6371ce7ba927",
      "version": "3.0.0.20500",
      "name": "RealTime",
      "status": "ENABLED",
      "tables": [
        "DEFAULT.REALTIMETABLE"
      ],
      "realizations": [
        {
          "type": "CUBE",
          "realization": "RealTime_cube"
        }
      ],
      "models": [
        "RealTime_model"
      ],
      "description": ""
    },
    {
      "uuid": "ebfc9993-b7a2-e73e-fa3a-3eb93f7830a6",
      "version": "3.0.0.20500",
      "name": "Refresh",
      "status": "ENABLED",
      "tables": [
        "DEFAULT.V_F_MO_STATS"
      ],
      "realizations": [
        {
          "type": "CUBE",
          "realization": "Refresh_cube"
        },
        {
          "type": "CUBE",
          "realization": "Imcremental_cube"
        }
      ],
      "models": [
        "Refresh_model",
        "Imcremental_model"
      ],
      "description": ""
    }
  ],
  "timestamp": "2020-04-30T09:07:20.261+0000"
}


cube
{
  "status": 0,
  "msg": "SUCCESS",
  "data": {
              "fullBuild": true,
              "name": "Refresh_cube",
              "segments": [
                {
                  "dateRangeEnd": "9999-04-30 17:37:28",
                  "dateRangeStart": "1970-01-01 08:00:00",
                  "name": "FULL_BUILD",
                  "sourceOffsetEnd": 0,
                  "sourceOffsetStart": 0,
                  "status": "READY",
                  "uuid": "c119f8fd-b219-9144-ad83-2a80996370d0"
                }
              ],
              "status": "READY",
              "streaming": false,
              "uuid": "2f50bfcf-98f2-9bc5-0982-f5b703f57da2",
              "version": "3.0.0.20500"
            },
  "timestamp": "2020-04-30T09:08:24.097+0000"
}

targetDataDto
{
    "cube":{
               "fullBuild": false,
               "name": "Imcremental_cube",
               "segments": [
                 {
                   "dateRangeStart": "2019-05-01 08:00:00",
                   "name": "20190501000000_20190531000000",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "27110503-497e-dc7b-7fd7-385559afa75c"
                 },
                 {
                   "dateRangeStart": "2019-05-31 08:00:00",
                   "name": "20190531000000_20200423170900",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "95dda247-d607-4c71-bcf9-5ba2b476301d"
                 },
                 {
                   "dateRangeStart": "2020-04-24 01:09:00",
                   "name": "20200423170900_20200423171200",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "e13a849d-8941-bae1-8198-b31bb66909a4"
                 },
                 {
                   "dateRangeStart": "2020-04-24 01:12:00",
                   "name": "20200423171200_20200423171500",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "635ddef0-a6d4-f775-2f17-2470b385cdb8"
                 },
                 {
                   "dateRangeStart": "2020-04-24 01:15:00",
                   "name": "20200423171500_20200423171800",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "6490cdb7-e04d-4051-3147-bd67a09f51e0"
                 },
                 {
                   "dateRangeStart": "2020-04-24 01:18:00",
                   "name": "20200423171800_20200424110000",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "7da2b67b-1aaf-9fde-7826-cb1474554060"
                 },
                 {
                   "dateRangeStart": "2020-04-24 19:00:00",
                   "name": "20200424110000_20200424110300",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "2abb5247-2eaa-840b-7a57-f461efa93692"
                 },
                 {
                   "dateRangeStart": "2020-04-24 19:03:00",
                   "name": "20200424110300_20200424110600",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "59b35fa6-f64a-f471-1120-abc192b8c32b"
                 },
                 {
                   "dateRangeStart": "2020-04-24 19:06:00",
                   "name": "20200424110600_20200424110900",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "55379e28-9d09-5fba-4551-71428870b5af"
                 },
                 {
                   "dateRangeStart": "2020-04-24 19:09:00",
                   "name": "20200424110900_20200424111200",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "ebcfd841-4023-a009-8ed1-e0bac0bfc406"
                 },
                 {
                   "dateRangeStart": "2020-04-24 19:12:00",
                   "name": "20200424111200_20200424111500",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "014eb012-f383-e21e-53e7-177e09084f31"
                 },
                 {
                   "dateRangeStart": "2020-04-24 19:15:00",
                   "name": "20200424111500_20200424111800",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "8ef1e35e-0421-ee71-762f-f34764f42bf8"
                 },
                 {
                   "dateRangeStart": "2020-04-24 19:18:00",
                   "name": "20200424111800_20200424132700",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "d18c4d73-e3ec-6b78-c432-14317b0b8155"
                 },
                 {
                   "dateRangeStart": "2020-04-24 21:27:00",
                   "name": "20200424132700_20200424160300",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "4965d3ab-357b-87cf-d702-902dcca4f4f2"
                 },
                 {
                   "dateRangeStart": "2020-04-25 00:03:00",
                   "name": "20200424160300_20200424160600",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "a723e74b-ef20-fd1c-5e8c-8a452633137a"
                 },
                 {
                   "dateRangeStart": "2020-04-25 00:06:00",
                   "name": "20200424160600_20200424160900",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "ac1f6e6b-1fd7-9f2a-a47f-4b3fa64f0d0f"
                 },
                 {
                   "dateRangeStart": "2020-04-25 00:09:00",
                   "name": "20200424160900_20200424161200",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "6279f2a4-124d-115c-f520-fc17119a3f68"
                 },
                 {
                   "dateRangeStart": "2020-04-25 00:12:00",
                   "name": "20200424161200_20200424161500",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "798f776c-28e0-463e-525e-56511e38c2d4"
                 },
                 {
                   "dateRangeStart": "2020-04-25 00:15:00",
                   "name": "20200424161500_20200424161800",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "602d7ee2-b4ef-3e03-78f7-0ba8c1d51b05"
                 },
                 {
                   "dateRangeStart": "2020-04-25 00:18:00",
                   "name": "20200424161800_20200424162100",
                   "sourceOffsetEnd": 0,
                   "sourceOffsetStart": 0,
                   "status": "READY",
                   "uuid": "e7466040-507e-8253-7445-064b4be41fef"
                 }
               ],
               "status": "READY",
               "streaming": false,
               "uuid": "4a0fbe24-ce86-eb6b-4387-c61d1153ac33",
               "version": "3.0.0.20500"
             },
     "segment": {
                        "dateRangeStart": "2020-04-25 00:18:00",
                        "name": "20200424161800_20200424162100",
                        "sourceOffsetEnd": 0,
                        "sourceOffsetStart": 0,
                        "status": "READY",
                        "uuid": "e7466040-507e-8253-7445-064b4be41fef"
                      },
      "action":"REFRESH"                             
}

{     "cube":{                "fullBuild": true,                "name": "Refresh_cube",                "segments": [                  {                    "dateRangeEnd": "9999-04-30 17:37:28",                    "dateRangeStart": "1970-01-01 08:00:00",                    "name": "FULL_BUILD",                    "sourceOffsetEnd": 0,                    "sourceOffsetStart": 0,                    "status": "READY",                    "uuid": "c119f8fd-b219-9144-ad83-2a80996370d0"                  }                ],                "status": "READY",                "streaming": false,                "uuid": "2f50bfcf-98f2-9bc5-0982-f5b703f57da2",                "version": "3.0.0.20500"              },      "segment": {                                    "dateRangeEnd": "9999-04-30 17:37:28",                                    "dateRangeStart": "1970-01-01 08:00:00",                                    "name": "FULL_BUILD",                                    "sourceOffsetEnd": 0,                                    "sourceOffsetStart": 0,                                    "status": "READY",                                    "uuid": "c119f8fd-b219-9144-ad83-2a80996370d0"                                  },       "action":"REFRESH"                              }

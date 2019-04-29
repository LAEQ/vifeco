require 'rest-client'
require 'json'
  
  
options = {
    token: "xoxp-521832980311-520675523571-611242433713-7b82ae9a38b8eba1e5a65ef7e15e5030",
    file: File.new("/home/david/vifeco/export/EmilePronovost-2019-04-24-14:52:25.zip", 'rb'),
    filename: "composed_test",
    title: "Composed ",
    channels: "vifeco"
}

res = RestClient.post 'https://slack.com/api/files.upload', options
json_response = JSON.parse(res.body)

# Return the uploaded file's ID
# thread_ts = json_response["file"]["shares"]["private"]["vifeco"]["ts"]
# file_id = json_response["file"]["id"]

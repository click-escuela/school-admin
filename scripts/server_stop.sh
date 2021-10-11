#!/bin/bash
chmod +x /home/ec2-user/server/school-admin/logs
chmod +x /home/ec2-user/server/school-admin/logs/error.log
chmod +x /home/ec2-user/server/school-admin/logs/debug.log
var="$(cat /home/ec2-user/server/school-admin/school-service.pid)"
sudo kill $var
sudo rm -rf /home/ec2-user/server/school-admin/school-service.pid


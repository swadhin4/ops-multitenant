<!DOCTYPE html>

<!--
Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License").

You may not use this file except in compliance with the License. A copy
of the License is located at

http://aws.amazon.com/apache2.0/

or in the "license" file accompanying this file. This file is distributed
on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
either express or implied. See the License for the specific language governing
permissions and limitations under the License.
-->

<html lang="en">
    <head>
        <title>AWS S3 Explorer</title>
        <!-- <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="https://aws.amazon.com/favicon.ico">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.1.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="https://cdn.datatables.net/plug-ins/f2c75b7247b/integration/bootstrap/3/dataTables.bootstrap.css"> -->
        <style type="text/css">
            #wrapper { padding-left: 0; }
            #page-wrapper { width: 100%; padding: 5px 15px; }
            #tb-s3objects { width: 100% !Important; }
            a { color: #00B7FF; }
            body { font: 14px "Lucida Grande", Helvetica, Arial, sans-serif; }
            td { font: 12px "Lucida Grande", Helvetica, Arial, sans-serif; }
        </style>
    </head>

    <!-- DEBUG: Enable this for red outline on all elements -->
    <!-- <style media="screen" type="text/css"> * { outline: 1px red solid; } </style> -->

    <body>
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-primary">

                        <!-- Panel including bucket/folder information and controls -->
                        <div class="panel-heading clearfix">
                            <!-- Bucket selection and breadcrumbs -->
                            <div class="btn-group pull-left">
                                <div class="pull-left">
                                    AWS S3 Explorer&nbsp;
                                </div>
                                <!-- Bucket selection -->
                                <div class="btn pull-left" id="bucket">
                                    <i id="bucket-chooser" style="cursor: pointer;" class="fa fa-bitbucket fa-2x" title="Switch to a different S3 Bucket"></i>
                                </div>
                                <!-- Bucket breadcrumbs -->
                                <div class="btn pull-right">
                                    <ul id="breadcrumb" class="btn breadcrumb pull-right">
                                        <li class="active dropdown">
                                            <a href="#">&lt;bucket&gt;</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <!-- Folder/Bucket radio group and progress spinner -->
                            <div class="btn-group pull-right">
                                <div class="checkbox pull-left">
                                    <label>
                                        <input type="checkbox" id="hidefolders">&nbsp;Hide folders?
                                    </label>
                                    <!-- Folder/Bucket radio group -->
                                    <div class="btn-group" data-toggle="buttons">
                                        <label class="btn btn-primary active" title="View all objects in folder">
                                            <i class="fa fa-angle-double-up"></i>
                                            <input type="radio" name="optionsdepth" value="folder" id="optionfolder" checked>&nbsp;Folder
                                        </label>
                                        <label class="btn btn-primary" title="View all objects in bucket">
                                            <i class="fa fa-angle-double-down"></i>
                                            <input type="radio" name="optionsdepth" value="bucket" id="optionbucket">&nbsp;Bucket
                                        </label>
                                    </div>
                                </div>
                                <!-- Dual purpose: progress spinner and refresh button -->
                                <div class="btn-group pull-right" id="refresh">
                                    <span id="bucket-loader" style="cursor: pointer;" class="btn fa fa-refresh fa-2x pull-left" title="Refresh"></span>
                                    <span id="badgecount" class="badge pull-right">42</span>
                                </div>
                            </div>
                        </div>

                        <!-- Panel including S3 object table -->
                        <div class="panel-body">
                            <table class="table table-bordered table-hover table-striped" id="tb-s3objects">
                                <thead>
                                    <tr>
                                        <th>Object</th>
                                        <th>Folder</th>
                                        <th>Last Modified</th>
                                        <th>Size</th>
                                    </tr>
                                </thead>
                                <tbody id="tbody-s3objects"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

<!-- <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->




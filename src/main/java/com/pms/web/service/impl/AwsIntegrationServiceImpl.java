package com.pms.web.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.pms.app.view.vo.CompanyAttachments;
import com.pms.web.service.AwsIntegrationService;
import com.pms.web.util.ApplicationUtil;
import com.pms.web.util.RestResponse;

@Service("awsIntegrationService")
public class AwsIntegrationServiceImpl implements AwsIntegrationService{
	
	private static final String SUFFIX = "/";
	private final static Logger LOGGER = LoggerFactory.getLogger(AwsIntegrationServiceImpl.class);
	@Override
	public void uploadObject(PutObjectRequest fileObject, AmazonS3 s3Client) {
		s3Client.putObject(fileObject);
	}

	@Override
	public void createFolder(String bucketName, String folderName, AmazonS3 s3Client) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent, metadata);

		// send request to S3 to create folder
		s3Client.putObject(putObjectRequest);
	}

	@Override
	public void deleteFolder(String bucketName, String folderName, AmazonS3 s3Client) {
		List<S3ObjectSummary> fileList = s3Client.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary file : fileList) {
			s3Client.deleteObject(bucketName, file.getKey());
		}
		s3Client.deleteObject(bucketName, folderName);
	}

	@Override
	public File downloadFile(String bucketName, String keyName) {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJZTA6BYNTESWQWBQ","YWzhoGSfC1ADDT+xHzvAsvf/wyMlSl71TexLLg8t");
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
		byte[] readBuf = new byte[1024];
		String imageBytes ="";
		File file=null;
		try{
			S3Object o=s3client.getObject(bucketName, keyName);
			S3ObjectInputStream s3is = o.getObjectContent();
			String contentType = o.getObjectMetadata().getContentType();
			String fileDownloadLocation = ApplicationUtil.getServerDownloadLocation();
			file=new File(fileDownloadLocation+"//"+keyName);
				FileOutputStream fos =  new FileOutputStream(file);
				int read_len = 0;
				while((read_len = s3is.read(readBuf))>0){
					fos.write(readBuf,0,read_len);
				}
				
				s3is.close();
				fos.close();
			//System.out.println(imageBytes);
			
		}catch(AmazonServiceException e){
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return file;
	}

	@Override
	public RestResponse deleteFile(String bucketName, String keyName) throws Exception {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJZTA6BYNTESWQWBQ","YWzhoGSfC1ADDT+xHzvAsvf/wyMlSl71TexLLg8t");
		AmazonS3 s3client = new AmazonS3Client(credentials);
		RestResponse response = new RestResponse();
		s3client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
		 try {
			 s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
			 response.setStatusCode(200);
	        } catch (AmazonServiceException ase) {
	          //  System.out.println("Caught an AmazonServiceException.");
	          //  System.out.println("Error Message:    " + ase.getMessage());
	         //   System.out.println("HTTP Status Code: " + ase.getStatusCode());
	          //  System.out.println("AWS Error Code:   " + ase.getErrorCode());
	          //  System.out.println("Error Type:       " + ase.getErrorType());
	          //  System.out.println("Request ID:       " + ase.getRequestId());
	            response.setStatusCode(500);
	        } catch (AmazonClientException ace) {
	         //   System.out.println("Caught an AmazonClientException.");
	         //   System.out.println("Error Message: " + ace.getMessage());
	            response.setStatusCode(500);
	        }
		 return response;
	}

	@Override
	public RestResponse deleteMultipleFile(List<KeyVersion> keys) throws Exception {
		RestResponse response = new RestResponse();
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJZTA6BYNTESWQWBQ","YWzhoGSfC1ADDT+xHzvAsvf/wyMlSl71TexLLg8t");
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
		try{
			List<Bucket> bucketList = s3client.listBuckets();
			if(bucketList != null){
				DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest("malay-first-s3-bucket-pms-test");
				multiObjectDeleteRequest.setKeys(keys);
				try {
				    DeleteObjectsResult delObjRes = s3client.deleteObjects(multiObjectDeleteRequest);
				    System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());
				    response.setStatusCode(200);			
				} catch (MultiObjectDeleteException e) {
				   response.setStatusCode(500);
				   e.printStackTrace();
				}
			}else{
				response.setStatusCode(503);
				response.setMessage("AWS service not available");
			}
		}catch(Exception e){
			LOGGER.info("AWS service not available ", e.getMessage() );
			response.setStatusCode(503);
			response.setMessage("AWS Service Not Available");
		}
		return response;
	}



@Override
public List<CompanyAttachments> listBucketObjects() {
    String bucketName = "malay-first-s3-bucket-pms-test";
    List<CompanyAttachments>  companyAttachments = new ArrayList<CompanyAttachments>();
    try {    
    	AWSCredentials credentials = new BasicAWSCredentials("AKIAJZTA6BYNTESWQWBQ","YWzhoGSfC1ADDT+xHzvAsvf/wyMlSl71TexLLg8t");
		AmazonS3 s3Client = new AmazonS3Client(credentials);
		s3Client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
        System.out.println("Listing objects");

        // maxKeys is set to 2 to demonstrate the use of
        // ListObjectsV2Result.getNextContinuationToken()
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
        ListObjectsV2Result result;
        do {
            result = s3Client.listObjectsV2(req);

            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            	CompanyAttachments copmanyAttachment = new CompanyAttachments();
            	copmanyAttachment.setImagePath(objectSummary.getKey());
            	copmanyAttachment.setImageSize( ApplicationUtil.bytesConverter(objectSummary.getSize()));
            	copmanyAttachment.setCreationDate(objectSummary.getLastModified().toString());
            	companyAttachments.add(copmanyAttachment);
                //System.out.printf(" - %s (size: %s)  %s \n", objectSummary.getKey(), ApplicationUtil.bytesConverter(objectSummary.getSize()), objectSummary.getLastModified() );
            }
            // If there are more than maxKeys keys in the bucket, get a continuation token
            // and list the next objects.
            String token = result.getNextContinuationToken();
            System.out.println("Next Continuation Token: " + token);
            req.setContinuationToken(token);
        } while (result.isTruncated());
    }
    catch(AmazonServiceException e) {
        // The call was transmitted successfully, but Amazon S3 couldn't process 
        // it, so it returned an error response.
        e.printStackTrace();
    }
    catch(SdkClientException e) {
        // Amazon S3 couldn't be contacted for a response, or the client
        // couldn't parse the response from Amazon S3.
        e.printStackTrace();
    }
	return companyAttachments;
}
	
}

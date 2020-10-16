package org.panacea.drmp.nag.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.nag.domain.graph.NetworkLayerAttackGraphRepr;
import org.panacea.drmp.nag.service.NAGPostOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;


@Slf4j
@Service
public class NAGPostOutputServiceImpl implements NAGPostOutputService {


	@Autowired
	private RestTemplate restTemplate;

	@Value("${networkLayerAttackGraph.endpoint}")
	private String networkLayerAttackGraphURL;

	@Value("${networkLayerAttackGraph.fn}")
	private String networkLayerAttackGraphFn;


	@Override
	public void postNetworkLayerAttackGraphRepr(NetworkLayerAttackGraphRepr repr) {

        // convert repr to file
//        String tempFilePath = "/tmp/" + networkLayerAttackGraphFn;
//        File file = new File(tempFilePath);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, repr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//		MultiValueMap<String, Object> body
//				= new LinkedMultiValueMap<>();
//		body.add("file", new FileSystemResource(tempFilePath));

		HttpEntity<NetworkLayerAttackGraphRepr> requestEntity
				= new HttpEntity<>(repr);

		String endPointUrl = networkLayerAttackGraphURL; //+'/'+repr.getSnapshotId()+'/';

		log.info("[NAG] POST networkLayerAttackGraphRepr to " + endPointUrl);
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		try {

			response = restTemplate
					.postForEntity(endPointUrl, requestEntity, String.class);
		} catch (HttpClientErrorException e) {

				System.out.println("Response from storage service: "  +response);
				byte[] bytes = ((HttpClientErrorException.BadRequest)e).getResponseBodyAsByteArray();


				//Convert byte[] to String
				String s = new String(bytes);

				log.error(s);
				e.printStackTrace();

		}

	}
}

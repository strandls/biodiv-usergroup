/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.mail_utility.model.EnumModel.FIELDS;
import com.strandls.mail_utility.model.EnumModel.INFO_FIELDS;
import com.strandls.mail_utility.model.EnumModel.INVITATION_DATA;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;
import com.strandls.mail_utility.model.EnumModel.REQUEST_DATA;
import com.strandls.mail_utility.producer.RabbitMQProducer;
import com.strandls.mail_utility.util.JsonUtil;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;
import com.strandls.userGroup.RabbitMqConnection;
import com.strandls.userGroup.pojo.InvitaionMailData;
import com.strandls.userGroup.pojo.UserGroupIbp;

/**
 * @author Abhishek Rudra
 *
 */
public class MailUtils {

	private final Logger logger = LoggerFactory.getLogger(MailUtils.class);

	@Inject
	private RabbitMQProducer producer;

	public void sendInvites(List<InvitaionMailData> mailDataList, String serverUrl) {

		try {
			for (InvitaionMailData mailData : mailDataList) {

				if (mailData.getInviteeEmail() != null) {
					Map<String, Object> data = new HashMap<String, Object>();
					data.put(FIELDS.TO.getAction(), new String[] { mailData.getInviteeEmail() });
					data.put(FIELDS.SUBSCRIPTION.getAction(), true);
					Map<String, Object> inviteData = new HashMap<String, Object>();

					inviteData.put(INVITATION_DATA.SERVER_URL.getAction(), serverUrl);
					inviteData.put(INVITATION_DATA.ENCRYPTED_KEY.getAction(), mailData.getToken());
					inviteData.put(INVITATION_DATA.GROUP_OBJ.getAction(), mailData.getUserGroup());
					inviteData.put(INVITATION_DATA.INVITEE_NAME.getAction(), mailData.getInviteeName());
					inviteData.put(INVITATION_DATA.INVITER_OBJ.getAction(), mailData.getInviterObject());
					inviteData.put(INVITATION_DATA.ROLE.getAction(), mailData.getRole());

					data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(inviteData));
					
					Map<String, Object> mData = new HashMap<String, Object>();
					mData.put(INFO_FIELDS.TYPE.getAction(), MAIL_TYPE.SEND_INVITE.getAction());
					mData.put(INFO_FIELDS.RECIPIENTS.getAction(), Arrays.asList(data));

					producer.produceMail(RabbitMqConnection.EXCHANGE_BIODIV,
							RabbitMqConnection.MAILING_ROUTINGKEY, null, JsonUtil.mapToJSON(mData));
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void sendRequest(List<User> requesteeDetails, UserIbp requesterObject, UserGroupIbp userGroupIbp,
			String encryptionKey, String serverUrl) {
		try {

			for (User requestee : requesteeDetails) {
				if (requestee.getEmail() != null) {
					Map<String, Object> data = new HashMap<String, Object>();
					data.put(FIELDS.TO.getAction(), new String[] { requestee.getEmail() });
					data.put(FIELDS.SUBSCRIPTION.getAction(), true);

					Map<String, Object> requestData = new HashMap<String, Object>();
					requestData.put(REQUEST_DATA.REQUESTEE_NAME.getAction(), requestee.getName());
					requestData.put(REQUEST_DATA.REQUESTOR.getAction(), requesterObject);
					requestData.put(REQUEST_DATA.SERVER_URL.getAction(), serverUrl);
					requestData.put(REQUEST_DATA.GROUP.getAction(), userGroupIbp);
					requestData.put(REQUEST_DATA.ENCRYPTED_KEY.getAction(), encryptionKey);

					data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(requestData));
					
					Map<String, Object> mData = new HashMap<String, Object>();
					mData.put(INFO_FIELDS.TYPE.getAction(), MAIL_TYPE.SEND_REQUEST.getAction());
					mData.put(INFO_FIELDS.RECIPIENTS.getAction(), Arrays.asList(data));

					producer.produceMail(RabbitMqConnection.EXCHANGE_BIODIV,
							RabbitMqConnection.MAILING_ROUTINGKEY, null, JsonUtil.mapToJSON(mData));

				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}

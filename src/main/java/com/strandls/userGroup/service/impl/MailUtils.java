/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.mail_utility.model.EnumModel.FIELDS;
import com.strandls.mail_utility.model.EnumModel.INVITATION_DATA;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;
import com.strandls.mail_utility.producer.RabbitMQProducer;
import com.strandls.mail_utility.util.JsonUtil;
import com.strandls.userGroup.RabbitMqConnection;
import com.strandls.userGroup.pojo.InvitaionMailData;

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

				if (!mailData.getInviteeEmail().contains("@ibp.org")) {
					Map<String, Object> data = new HashMap<String, Object>();
					data.put(FIELDS.TYPE.getAction(), MAIL_TYPE.SEND_INVITE.getAction());
					data.put(FIELDS.TO.getAction(), new String[] { mailData.getInviteeEmail() });
					Map<String, Object> inviteData = new HashMap<String, Object>();

					inviteData.put(INVITATION_DATA.SERVER_URL.getAction(), serverUrl);
					inviteData.put(INVITATION_DATA.ENCRYPTED_KEY.getAction(), mailData.getToken());
					inviteData.put(INVITATION_DATA.GROUP_OBJ.getAction(), mailData.getUserGroup());
					inviteData.put(INVITATION_DATA.INVITEE_NAME.getAction(), mailData.getInviteeName());
					inviteData.put(INVITATION_DATA.INVITER_OBJ.getAction(), mailData.getInviterObject());
					inviteData.put(INVITATION_DATA.ROLE.getAction(), mailData.getRole());

					data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(inviteData));

					producer.produceNotification(RabbitMqConnection.EXCHANGE_BIODIV,
							RabbitMqConnection.MAILING_ROUTINGKEY, null, JsonUtil.mapToJSON(data));
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}

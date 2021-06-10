package org.example.quarkus;

import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@Entity
public class Message extends PanacheEntity {

	public String recipient;
	public String sender;
	public String content;

	public Message() {
	}

	public Message(@NotBlank String recipient, @NotBlank String sender,
			@NotNull String content) {
		this.recipient = recipient;
		this.sender = sender;
		this.content = content;
	}

	public static List<Message> getAllMessagesOfUser(String user) {
		// Short of: "from MESSAGE where recipient = ?1
		return list("recipient", user);
	}

	public static Message getSpecificMessage(long id) {
		return findById(id);
	}

	public static Message save(Message message) {
		persist(message);
		return message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((recipient == null) ? 0 : recipient.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (recipient == null) {
			if (other.recipient != null)
				return false;
		} else if (!recipient.equals(other.recipient))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}


}

/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guestbook;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * A guestbook entry. An entity as in the Domain Driven Design context. Mapped onto the database using JPA annotations.
 *
 * @author Paul Henke
 * @author Oliver Drotbohm
 * @see https://en.wikipedia.org/wiki/Domain-driven_design#Building_blocks
 */
@Entity
class GuestbookEntry {

	private @Id @GeneratedValue Long id;
	private final String name, text;
	private final LocalDateTime date;
	private String Liked = "%";
	private String Disliked = "%";
	/**
	 * Creates a new {@link GuestbookEntry} for the given name and text.
	 *
	 * @param name must not be {@literal null} or empty
	 * @param text must not be {@literal null} or empty
	 */
	public GuestbookEntry(String name, String text) {

		Assert.hasText(name, "Name must not be null or empty!");
		Assert.hasText(text, "Text must not be null or empty!");
		
		this.name = name;
		this.text = text;
		this.date = LocalDateTime.now();
	}

	@SuppressWarnings("unused")
	private GuestbookEntry() {
		this.name = null;
		this.text = null;
		this.date = null;
	}

	public void Like(String username) {
		if(Liked.contains("%"+username+"%")) {
			Liked = Liked.replace("%"+username+"%", "%");
		}else {
			Disliked = Disliked.replace("%"+username+"%", "%");
			Liked += username+"%";
		}
	}
	public void Dislike(String username) {
		if(Disliked.contains("%"+username+"%")) {
			Disliked = Disliked.replace("%"+username+"%", "%");
		}else {
			Liked = Liked.replace("%"+username+"%", "%");
			Disliked += username+"%";
		}
	}
	public String getLikeColor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(Liked.contains("%"+auth.getName()+"%")) {return "color:blue";}
		return "color:black";
	}
	public String getDislikeColor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(Disliked.contains("%"+auth.getName()+"%")) {return "color:blue";}
		return "color:black";
	}
	public int getRating() {
		return Liked.split("%", -1).length - Disliked.split("%", -1).length;
	}
	
	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getText() {
		return text;
	}
}

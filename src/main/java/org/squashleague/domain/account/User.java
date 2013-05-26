package org.squashleague.domain.account;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.league.Player;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jamesdbloom
 */
@Entity
public class User extends ModelObject {

    public static final String PASSWORD_PATTERN = "^.*(?=.{8,})(?=.*\\d)(?=.*(\\£|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|\\)|\\-|\\_|\\[|\\]|\\{|\\}|\\<|\\>|\\~|\\`|\\+|\\=|\\,|\\.|\\;|\\:|\\/|\\?|\\|))(?=.*[a-zA-Z]).*$";
    public static final String EMAIL_PATTERN = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
    // basic properties
    @NotNull(message = "{validation.user.name}")
    @Size(min = 3, max = 50, message = "{validation.user.name}")
    private String name;
    @Column(unique = true)
    @NotNull(message = "{validation.user.email}")
    @Pattern(regexp = EMAIL_PATTERN, message = "{validation.user.email}")
    @NotEmpty(message = "{validation.user.email}")
    private String email;
    @Pattern(regexp = "( *\\d *){6,15}", message = "{validation.user.mobile}")
    private String mobile;
    @NotNull(message = "{validation.user.mobilePrivacy}")
    private MobilePrivacy mobilePrivacy;
    // login
    @NotNull(message = "{validation.user.roles}")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn
    private List<Role> roles = Lists.newArrayList(Role.ROLE_ANONYMOUS);
    private String password;
    private String oneTimeToken;
    // extra domain traversal
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Player> players;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User withName(String name) {
        setName(name);
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User withEmail(String email) {
        setEmail(email);
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public User withMobile(String mobile) {
        setMobile(mobile);
        return this;
    }

    public MobilePrivacy getMobilePrivacy() {
        return mobilePrivacy;
    }

    public void setMobilePrivacy(MobilePrivacy mobilePrivacy) {
        this.mobilePrivacy = mobilePrivacy;
    }

    public User withMobilePrivacy(MobilePrivacy mobilePrivacy) {
        setMobilePrivacy(mobilePrivacy);
        return this;
    }

    public String[] getRoleNames() {
        String[] roleName = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            roleName[i] = roles.get(i).getName();
        }
        return roleName;
    }

    public boolean hasRole(Role role) {
        return roles != null && roles.contains(role);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public User withRole(Role... role) {
        setRoles(Lists.newArrayList(role));
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User withPassword(String password) {
        setPassword(password);
        return this;
    }

    public String getOneTimeToken() {
        return oneTimeToken;
    }

    public void setOneTimeToken(String oneTimeToken) {
        this.oneTimeToken = oneTimeToken;
    }

    public User withOneTimeToken(String oneTimeToken) {
        setOneTimeToken(oneTimeToken);
        return this;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public User withPlayers(Player... players) {
        this.players = new ArrayList<>();
        for (Player player : players) {
            this.players.add(player.withUser(this));
        }
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "logger", "players");
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof User && new EqualsBuilder()
                .append(name, ((User) other).name)
                .append(email, ((User) other).email)
                .append(mobile, ((User) other).mobile)
                .append(mobilePrivacy, ((User) other).mobilePrivacy)
                .append(password, ((User) other).password)
                .append(roles.toArray(), ((User) other).roles.toArray())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "players");
    }
}

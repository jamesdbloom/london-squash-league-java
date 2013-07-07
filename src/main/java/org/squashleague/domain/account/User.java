package org.squashleague.domain.account;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;
import org.squashleague.domain.ModelObject;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.Player;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jamesdbloom
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends ModelObject<User> {

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
    @ManyToMany
    @JoinColumn
    private List<Role> roles = Lists.newArrayList(Role.ROLE_ANONYMOUS);
    private String password;
    private String oneTimeToken;
    // extra domain traversal
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Player> players;
    // view collections
    @Transient
    private transient Collection<Division> divisions;


    public boolean showMobileToOpponent() {
        return mobilePrivacy == MobilePrivacy.SHOW_ALL || mobilePrivacy == MobilePrivacy.SHOW_OPPONENTS;
    }

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

    public User withRoles(List<Role> roles) {
        setRoles(roles);
        return this;
    }

    public User withRoles(Role... roles) {
        setRoles(Lists.newArrayList(roles));
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

    public Collection<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(Collection<Division> divisions) {
        this.divisions = divisions;
    }

    public User merge(User user) {
        if (user.name != null) {
            this.name = user.name;
        }
        if (user.email != null) {
            this.email = user.email;
        }
        if (user.mobile != null) {
            this.mobile = user.mobile;
        }
        if (user.mobilePrivacy != null) {
            this.mobilePrivacy = user.mobilePrivacy;
        }
        if (user.password != null) {
            this.password = user.password;
        }
        if (user.oneTimeToken != null) {
            this.oneTimeToken = user.oneTimeToken;
        }
        if (user.roles != null) {
            this.roles = user.roles;
        }
        if (user.players != null) {
            this.players = user.players;
        }
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "logger", "roles", "players");
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof User && new EqualsBuilder()
                .append(getId(), ((User) other).getId())
                .append(getVersion(), ((User) other).getVersion())
                .append(name, ((User) other).name)
                .append(email, ((User) other).email)
                .append(mobile, ((User) other).mobile)
                .append(mobilePrivacy, ((User) other).mobilePrivacy)
                .append(password, ((User) other).password)
                .append(oneTimeToken, ((User) other).oneTimeToken)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "roles", "players");
    }
}

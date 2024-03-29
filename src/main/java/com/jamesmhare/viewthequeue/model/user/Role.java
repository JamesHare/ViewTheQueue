package com.jamesmhare.viewthequeue.model.user;

import com.jamesmhare.viewthequeue.model.AbstractTimestampedEntity;
import lombok.*;

import javax.persistence.*;

/**
 * Represents a Role that will be used to grant and restrict User access.
 *
 * @author James Hare
 */
@Generated
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role extends AbstractTimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long roleId;

    @Column(nullable = false)
    private String name;

}

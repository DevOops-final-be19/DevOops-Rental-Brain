package com.devoops.rentalbrain.common.notice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column
    private String type;
    @Column
    private String title;
    @Column
    private String message;
    @Column
    private String linkUrl;
}

/**
 * 点赞记录实体类
 * 对应数据库中的like_record表
 */
export interface LikeRecord {
    /**
     * 点赞记录ID
     */
    id: number | null;

    /**
     * 点赞用户ID（关联user.id）
     */
    userId: number | null;

    /**
     * 点赞的主留言ID（关联message.id）
     */
    messageId: number | null;

    /**
     * 点赞时间戳（毫秒）
     */
    createTime: number | null;

    /**
     * 取消状态：0-未取消 1-已取消
     */
    isCanceled: number | null;
}

/**
 * 留言实体类
 * 对应数据库中的message表
 */
export interface Message {
    /**
     * 留言唯一ID
     */
    id: number | null;

    /**
     * 留言标题
     */
    title: string | null;

    /**
     * 留言内容（支持富文本）
     */
    content: string | null;

    /**
     * 获赞数
     */
    likeCount: number | null;

    /**
     * 创建者ID（关联user.id）
     */
    creatorId: number | null;

    /**
     * 创建时间戳（毫秒）
     */
    createTime: number | null;

    /**
     * 更新时间戳（毫秒）
     */
    updateTime: number | null;

    /**
     * 状态：-2-违规 -1-待审核/停用 0-正常
     */
    status: number | null;

    /**
     * 是否删除：0-否 1-是
     */
    isDeleted: number | null;
}

/**
 * 回复实体类
 * 对应数据库中的reply表
 */
export interface Reply {
    /**
     * 回复唯一ID
     */
    id: number | null;

    /**
     * 所属主留言ID（关联message.id）
     */
    messageId: number | null;

    /**
     * 回复者ID（关联user.id）
     */
    creatorId: number | null;

    /**
     * 回复父ID：NULL-回复主留言 非NULL-多级回复
     */
    parentId: number | null;

    /**
     * 回复内容
     */
    content: string | null;

    /**
     * 创建时间戳（毫秒）
     */
    createTime: number | null;

    /**
     * 是否删除：0-否 1-是
     */
    isDeleted: number | null;
}

/**
 * 举报记录实体类
 * 对应数据库中的report表
 */
export interface Report {
    /**
     * 举报记录ID
     */
    id: number | null;

    /**
     * 举报用户ID（关联user.id）
     */
    userId: number | null;

    /**
     * 举报的主留言ID（关联message.id）
     */
    messageId: number | null;

    /**
     * 举报原因
     */
    reason: string | null;

    /**
     * 举报时间戳（毫秒）
     */
    createTime: number | null;

    /**
     * 审核状态：0-待审核 1-审核通过 2-审核驳回
     */
    auditStatus: number | null;

    /**
     * 审核管理员ID（关联user.id）
     */
    auditUserId: number | null;

    /**
     * 审核时间戳（毫秒）
     */
    auditTime: number | null;
}

/**
 * 用户实体类
 * 对应数据库中的user表
 */
export interface User {
    /**
     * 用户唯一ID
     */
    id: number | null;

    /**
     * 用户邮箱（登录账号）
     */
    email: string | null;

    /**
     * 用户昵称
     */
    nickname: string | null;

    /**
     * 密码（建议MD5/BCrypt加密存储）
     */
    password: string | null;

    /**
     * 角色：0-普通用户 1-管理员
     */
    role: number | null;

    /**
     * 状态：-1-停用 0-正常
     */
    status: number | null;

    /**
     * 创建时间戳（毫秒）
     */
    createTime: number | null;

    /**
     * 是否删除：0-否 1-是
     */
    isDeleted: number | null;
}

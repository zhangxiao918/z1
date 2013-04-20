
package com.skymobi.cac.maopao.xip;

import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.bakcpack.GetBgCompoundListReq;
import com.skymobi.cac.maopao.xip.bto.bakcpack.GetBgCompoundListResp;
import com.skymobi.cac.maopao.xip.bto.bakcpack.GetBgPropListReq;
import com.skymobi.cac.maopao.xip.bto.bakcpack.GetBgPropListResp;
import com.skymobi.cac.maopao.xip.bto.bakcpack.SortBpPropListReq;
import com.skymobi.cac.maopao.xip.bto.bakcpack.SortBpPropListResp;
import com.skymobi.cac.maopao.xip.bto.basis.EnterRoomStyleReq;
import com.skymobi.cac.maopao.xip.bto.basis.EnterRoomStyleResp;
import com.skymobi.cac.maopao.xip.bto.basis.GetGameInfoReq;
import com.skymobi.cac.maopao.xip.bto.basis.GetGameInfoResp;
import com.skymobi.cac.maopao.xip.bto.game.AddPlayerNotify;
import com.skymobi.cac.maopao.xip.bto.game.BidFinishNotify;
import com.skymobi.cac.maopao.xip.bto.game.BidNotify;
import com.skymobi.cac.maopao.xip.bto.game.BidReq;
import com.skymobi.cac.maopao.xip.bto.game.BidResp;
import com.skymobi.cac.maopao.xip.bto.game.DispatchCardNotify;
import com.skymobi.cac.maopao.xip.bto.game.PlayCardNotify;
import com.skymobi.cac.maopao.xip.bto.game.PlayCardReq;
import com.skymobi.cac.maopao.xip.bto.game.QuitGameReq;
import com.skymobi.cac.maopao.xip.bto.game.QuitGameResp;
import com.skymobi.cac.maopao.xip.bto.game.RaiseHandNotify;
import com.skymobi.cac.maopao.xip.bto.game.RaiseHandReq;
import com.skymobi.cac.maopao.xip.bto.game.RaiseHandResp;
import com.skymobi.cac.maopao.xip.bto.game.StandupNotify;
import com.skymobi.cac.maopao.xip.bto.game.TrusteeshipNotify;
import com.skymobi.cac.maopao.xip.bto.game.TrusteeshipReq;
import com.skymobi.cac.maopao.xip.bto.game.VictoryNotify;
import com.skymobi.cac.maopao.xip.bto.game.skill.AntiShowPlayerCardNotify;
import com.skymobi.cac.maopao.xip.bto.game.skill.AntiShowPlayerCardReq;
import com.skymobi.cac.maopao.xip.bto.game.skill.AntiShowPlayerCardResp;
import com.skymobi.cac.maopao.xip.bto.game.skill.AntiSwitchPlayerCardNotify;
import com.skymobi.cac.maopao.xip.bto.game.skill.AntiSwitchPlayerCardReq;
import com.skymobi.cac.maopao.xip.bto.game.skill.AntiSwitchPlayerCardResp;
import com.skymobi.cac.maopao.xip.bto.game.skill.DoubleNotify;
import com.skymobi.cac.maopao.xip.bto.game.skill.DoubleReq;
import com.skymobi.cac.maopao.xip.bto.game.skill.DoubleResp;
import com.skymobi.cac.maopao.xip.bto.game.skill.HalfNotify;
import com.skymobi.cac.maopao.xip.bto.game.skill.HalfReq;
import com.skymobi.cac.maopao.xip.bto.game.skill.HalfResp;
import com.skymobi.cac.maopao.xip.bto.game.skill.ShowBackCardReq;
import com.skymobi.cac.maopao.xip.bto.game.skill.ShowBackCardResp;
import com.skymobi.cac.maopao.xip.bto.game.skill.ShowPlayerCardNotify;
import com.skymobi.cac.maopao.xip.bto.game.skill.ShowPlayerCardReq;
import com.skymobi.cac.maopao.xip.bto.game.skill.SwitchPlayerCardNotify;
import com.skymobi.cac.maopao.xip.bto.game.skill.SwitchPlayerCardReq;
import com.skymobi.cac.maopao.xip.bto.im.ChatNotify;
import com.skymobi.cac.maopao.xip.bto.im.ChatReq;
import com.skymobi.cac.maopao.xip.bto.im.GetSiteMailContentReq;
import com.skymobi.cac.maopao.xip.bto.im.GetSiteMailContentResp;
import com.skymobi.cac.maopao.xip.bto.im.GetSiteMailInboxReq;
import com.skymobi.cac.maopao.xip.bto.im.GetSiteMailInboxResp;
import com.skymobi.cac.maopao.xip.bto.im.QueryNewSiteMailCountsReq;
import com.skymobi.cac.maopao.xip.bto.im.QueryNewSiteMailCountsResp;
import com.skymobi.cac.maopao.xip.bto.im.SendSiteMailReq;
import com.skymobi.cac.maopao.xip.bto.im.SendSiteMailResp;
import com.skymobi.cac.maopao.xip.bto.lobby.LaunchGameNotify;
import com.skymobi.cac.maopao.xip.bto.mall.GetMallListReq;
import com.skymobi.cac.maopao.xip.bto.mall.GetMallListResp;
import com.skymobi.cac.maopao.xip.bto.mall.PayMallReq;
import com.skymobi.cac.maopao.xip.bto.mall.PayMallResp;
import com.skymobi.cac.maopao.xip.bto.payment.CardChargeReq;
import com.skymobi.cac.maopao.xip.bto.payment.CardPayInfoListReq;
import com.skymobi.cac.maopao.xip.bto.payment.CardPayInfoListResp;
import com.skymobi.cac.maopao.xip.bto.payment.CardPayReq;
import com.skymobi.cac.maopao.xip.bto.payment.CardPayResp;
import com.skymobi.cac.maopao.xip.bto.payment.PaySwitchReq;
import com.skymobi.cac.maopao.xip.bto.payment.PaySwitchResp;
import com.skymobi.cac.maopao.xip.bto.payment.SMSPayAccessOrderReq;
import com.skymobi.cac.maopao.xip.bto.payment.SMSPayAccessOrderResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetAddReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetAddResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetBattleBoudoirReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetBattleBoudoirResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetClothesChangeReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetClothesChangeResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetDelReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetDelResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetDropPriceChangeReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetDropPriceChangeResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetExpReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetExpResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetOperReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetOperResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetShowReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetShowResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetSkillReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetSkillResp;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetSkillStudyReq;
import com.skymobi.cac.maopao.xip.bto.pet.PlayerPetSkillStudyResp;
import com.skymobi.cac.maopao.xip.bto.sys.GetFileServerAddrReq;
import com.skymobi.cac.maopao.xip.bto.sys.GetFileServerAddrResp;
import com.skymobi.cac.maopao.xip.bto.task.accept.GetPlayerTaskListReq;
import com.skymobi.cac.maopao.xip.bto.task.accept.GetPlayerTaskListResp;
import com.skymobi.cac.maopao.xip.bto.task.challenge.AcquireChallengeTaskReq;
import com.skymobi.cac.maopao.xip.bto.task.challenge.AcquireChallengeTaskResp;
import com.skymobi.cac.maopao.xip.bto.task.challenge.GetPlayerChallengeTaskListReq;
import com.skymobi.cac.maopao.xip.bto.task.challenge.GetPlayerChallengeTaskListResp;
import com.skymobi.cac.maopao.xip.bto.task.daily.GetPlayerDailyTaskListReq;
import com.skymobi.cac.maopao.xip.bto.task.daily.GetPlayerDailyTaskListResp;
import com.skymobi.cac.maopao.xip.bto.user.BindPhoneReq;
import com.skymobi.cac.maopao.xip.bto.user.BindPhoneResp;
import com.skymobi.cac.maopao.xip.bto.user.FindPasswordReq;
import com.skymobi.cac.maopao.xip.bto.user.FindPasswordResp;
import com.skymobi.cac.maopao.xip.bto.user.GetPlayerBaseInfoReq;
import com.skymobi.cac.maopao.xip.bto.user.GetPlayerBaseInfoResp;
import com.skymobi.cac.maopao.xip.bto.user.GetPlayerGameInfoReq;
import com.skymobi.cac.maopao.xip.bto.user.GetPlayerGameInfoResp;
import com.skymobi.cac.maopao.xip.bto.user.LoginReq;
import com.skymobi.cac.maopao.xip.bto.user.LoginResp;
import com.skymobi.cac.maopao.xip.bto.user.ModifyPasswordReq;
import com.skymobi.cac.maopao.xip.bto.user.ModifyPasswordResp;
import com.skymobi.cac.maopao.xip.bto.user.QuickRegisterReq;
import com.skymobi.cac.maopao.xip.bto.user.QuickRegisterResp;
import com.skymobi.cac.maopao.xip.bto.user.RegisterReq;
import com.skymobi.cac.maopao.xip.bto.user.RegisterResp;
import com.skymobi.cac.maopao.xip.bto.user.SwitchAccountReq;
import com.skymobi.cac.maopao.xip.bto.user.SwitchAccountResp;
import com.skymobi.cac.maopao.xip.bto.user.ThirdLoginReq;
import com.skymobi.cac.maopao.xip.bto.user.ThirdLoginResp;
import com.skymobi.cac.maopao.xip.bto.user.UserReportReq;
import com.skymobi.cac.maopao.xip.bto.user.UserReportResp;

import java.util.HashMap;
import java.util.Map;

public class XipBeanLoader {

    public static Map<Integer, Class<?>> msgCodeXipMap = new HashMap<Integer, Class<?>>();

    public static void add2Map(Class<?> cls) {
        if (XipBody.class.isAssignableFrom(cls)) {
            XipSignal attr = cls.getAnnotation(XipSignal.class);
            if (null != attr) {
                msgCodeXipMap.put(attr.messageCode(), cls);
            }
        }
    }

    static {
        // 登录与注册
        add2Map(LoginReq.class);
        add2Map(LoginResp.class);
        add2Map(RegisterReq.class);
        add2Map(RegisterResp.class);
        add2Map(QuickRegisterReq.class);
        add2Map(QuickRegisterResp.class);
        add2Map(ThirdLoginReq.class);
        add2Map(ThirdLoginResp.class);

        // Basis模块
        add2Map(GetGameInfoReq.class);
        add2Map(GetGameInfoResp.class);
        add2Map(EnterRoomStyleReq.class);
        add2Map(EnterRoomStyleResp.class);
        add2Map(UserReportReq.class);
        add2Map(UserReportResp.class);

        // 打牌游戏模块
        add2Map(LaunchGameNotify.class);

        add2Map(QuitGameReq.class);
        add2Map(QuitGameResp.class);

        add2Map(AddPlayerNotify.class);
        add2Map(RaiseHandNotify.class);
        add2Map(RaiseHandReq.class);
        add2Map(RaiseHandResp.class);

        add2Map(DispatchCardNotify.class);
        add2Map(StandupNotify.class);

        add2Map(BidNotify.class);
        add2Map(BidFinishNotify.class);
        add2Map(BidReq.class);
        add2Map(BidResp.class);

        add2Map(PlayCardReq.class);
        add2Map(PlayCardNotify.class);

        add2Map(VictoryNotify.class);

        add2Map(TrusteeshipReq.class);
        add2Map(TrusteeshipNotify.class);

        // 游戏技能
        add2Map(ShowBackCardReq.class);
        add2Map(ShowBackCardResp.class);
        add2Map(DoubleNotify.class);
        add2Map(DoubleReq.class);
        add2Map(DoubleResp.class);
        add2Map(HalfNotify.class);
        add2Map(HalfReq.class);
        add2Map(HalfResp.class);
        add2Map(AntiShowPlayerCardNotify.class);
        add2Map(AntiShowPlayerCardReq.class);
        add2Map(AntiShowPlayerCardResp.class);
        add2Map(AntiSwitchPlayerCardNotify.class);
        add2Map(AntiSwitchPlayerCardReq.class);
        add2Map(AntiSwitchPlayerCardResp.class);
        add2Map(ShowPlayerCardNotify.class);
        add2Map(ShowPlayerCardReq.class);
        add2Map(SwitchPlayerCardNotify.class);
        add2Map(SwitchPlayerCardReq.class);

        // 支付模块
        add2Map(CardPayInfoListReq.class);
        add2Map(CardPayInfoListResp.class);
        add2Map(CardPayReq.class);
        add2Map(CardChargeReq.class);
        add2Map(CardPayResp.class);
        add2Map(SMSPayAccessOrderReq.class);
        add2Map(SMSPayAccessOrderResp.class);
        add2Map(PaySwitchReq.class);
        add2Map(PaySwitchResp.class);

        // 任务模块
        // 挑战任务
        add2Map(GetPlayerChallengeTaskListReq.class);
        add2Map(GetPlayerChallengeTaskListResp.class);

        // 接收挑战任务
        add2Map(AcquireChallengeTaskReq.class);
        add2Map(AcquireChallengeTaskResp.class);

        // 日常任务
        add2Map(GetPlayerDailyTaskListReq.class);
        add2Map(GetPlayerDailyTaskListResp.class);

        // 已接任务
        add2Map(GetPlayerTaskListReq.class);
        add2Map(GetPlayerTaskListResp.class);
        /**
         * 女宠模块
         */
        add2Map(PlayerPetAddReq.class);
        add2Map(PlayerPetAddResp.class);
        add2Map(PlayerPetBattleBoudoirReq.class);
        add2Map(PlayerPetBattleBoudoirResp.class);
        add2Map(PlayerPetClothesChangeReq.class);
        add2Map(PlayerPetClothesChangeResp.class);
        add2Map(PlayerPetDelReq.class);
        add2Map(PlayerPetDelResp.class);
        add2Map(PlayerPetDropPriceChangeReq.class);
        add2Map(PlayerPetDropPriceChangeResp.class);
        add2Map(PlayerPetExpReq.class);
        add2Map(PlayerPetExpResp.class);
        add2Map(PlayerPetOperReq.class);
        add2Map(PlayerPetOperResp.class);
        add2Map(PlayerPetShowReq.class);
        add2Map(PlayerPetShowResp.class);
        add2Map(PlayerPetSkillReq.class);
        add2Map(PlayerPetSkillResp.class);
        add2Map(PlayerPetSkillStudyReq.class);
        add2Map(PlayerPetSkillStudyResp.class);

        // 修改密码
        add2Map(ModifyPasswordReq.class);
        add2Map(ModifyPasswordResp.class);

        // 获取用户信息
        add2Map(GetPlayerBaseInfoReq.class);
        add2Map(GetPlayerBaseInfoResp.class);

        // 绑定手机
        add2Map(BindPhoneReq.class);
        add2Map(BindPhoneResp.class);

        // 忘记密码
        add2Map(FindPasswordReq.class);
        add2Map(FindPasswordResp.class);

        /**
         * 商城模块
         */
        // 获取商城数据列表请求
        add2Map(GetMallListReq.class);
        add2Map(GetMallListResp.class);
        add2Map(PayMallReq.class);
        add2Map(PayMallResp.class);

        /**
         * 背包模块
         */
        add2Map(GetBgCompoundListReq.class);
        add2Map(GetBgCompoundListResp.class);
        add2Map(GetBgPropListReq.class);
        add2Map(GetBgPropListResp.class);
        add2Map(SortBpPropListReq.class);
        add2Map(SortBpPropListResp.class);

        /**
         * 站内信
         */
        add2Map(GetSiteMailContentReq.class);
        add2Map(GetSiteMailContentResp.class);
        add2Map(GetSiteMailInboxReq.class);
        add2Map(GetSiteMailInboxResp.class);
        add2Map(QueryNewSiteMailCountsReq.class);
        add2Map(QueryNewSiteMailCountsResp.class);
        add2Map(SendSiteMailReq.class);
        add2Map(SendSiteMailResp.class);

        /**
         * 聊天模块
         */
        add2Map(ChatReq.class);
        add2Map(ChatNotify.class);

        /**
         * 获取用户游戏信息
         */
        add2Map(GetPlayerGameInfoReq.class);
        add2Map(GetPlayerGameInfoResp.class);

        /**
         * 获取图片地址
         */
        add2Map(GetFileServerAddrReq.class);
        add2Map(GetFileServerAddrResp.class);

        /**
         * 切换帐号
         */
        add2Map(SwitchAccountReq.class);
        add2Map(SwitchAccountResp.class);

    }
}


package android.databinding;
import youdrive.today.BR;
class DataBinderMapper {
    final static int TARGET_MIN_SDK = 16;
    public DataBinderMapper() {
    }
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View view, int layoutId) {
        switch(layoutId) {
                case youdrive.today.R.layout.fragment_register_profile:
                    return youdrive.today.databinding.FragmentRegisterProfileBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.fragment_register_payments:
                    return youdrive.today.databinding.FragmentRegisterPaymentsBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_complete:
                    return youdrive.today.databinding.ActivityCompleteBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.fragment_about_fourth:
                    return youdrive.today.databinding.FragmentAboutFourthBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.popup_close_car:
                    return youdrive.today.databinding.DialogCloseCar.bind(view, bindingComponent);
                case youdrive.today.R.layout.item_profile:
                    return youdrive.today.databinding.ItemProfileBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.marker_info:
                    return youdrive.today.databinding.MarkerInfo.bind(view, bindingComponent);
                case youdrive.today.R.layout.fragment_about_second:
                    return youdrive.today.databinding.FragmentAboutSecondBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.fragment_register_offert:
                    return youdrive.today.databinding.FragmentRegisterOffertBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_confirmation:
                    return youdrive.today.databinding.ActivityConfirmationBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_wellcome:
                    return youdrive.today.databinding.ActivityWellcomeBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.fragment_about_first:
                    return youdrive.today.databinding.FragmentAboutFirstBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.item_phone_invite:
                    return youdrive.today.databinding.ItemPhoneInviteBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_referal:
                    return youdrive.today.databinding.ActivityReferalBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.fragment_about_third:
                    return youdrive.today.databinding.FragmentAboutThirdBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_thanks:
                    return youdrive.today.databinding.ActivityThanksBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.popup_open_car:
                    return youdrive.today.databinding.OpenCarDialog.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_inform:
                    return youdrive.today.databinding.ActivityInformBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.popup_distance:
                    return youdrive.today.databinding.PopupDistance.bind(view, bindingComponent);
                case youdrive.today.R.layout.dialog_info_contents:
                    return youdrive.today.databinding.DialogInfo.bind(view, bindingComponent);
                case youdrive.today.R.layout.fragment_register_documents:
                    return youdrive.today.databinding.FragmentRegisterDocumentsBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_search_car:
                    return youdrive.today.databinding.ActivitySearchCarBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.item_email:
                    return youdrive.today.databinding.ItemEmailBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_registration:
                    return youdrive.today.databinding.ActivityRegistrationBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_login:
                    return youdrive.today.databinding.ActivityLoginBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.header_profile:
                    return youdrive.today.databinding.HeaderProfileBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.fragment_payment:
                    return youdrive.today.databinding.FragmentPaymentBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.item_image:
                    return youdrive.today.databinding.ItemImageBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_invite:
                    return youdrive.today.databinding.ActivityInviteBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.item_popup:
                    return youdrive.today.databinding.ItemPopupBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.fragment_referal:
                    return youdrive.today.databinding.FragmentReferalBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_maps:
                    return youdrive.today.databinding.ActivityMapsBinding.bind(view, bindingComponent);
                case youdrive.today.R.layout.activity_order_car:
                    return youdrive.today.databinding.ActivityOrderCarBinding.bind(view, bindingComponent);
        }
        return null;
    }
    android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View[] views, int layoutId) {
        switch(layoutId) {
        }
        return null;
    }
    int getLayoutId(String tag) {
        if (tag == null) {
            return 0;
        }
        final int code = tag.hashCode();
        switch(code) {
            case -378028696: {
                if(tag.equals("layout/fragment_register_profile_0")) {
                    return youdrive.today.R.layout.fragment_register_profile;
                }
                break;
            }
            case -413409520: {
                if(tag.equals("layout/fragment_register_payments_0")) {
                    return youdrive.today.R.layout.fragment_register_payments;
                }
                break;
            }
            case -953080971: {
                if(tag.equals("layout/activity_complete_0")) {
                    return youdrive.today.R.layout.activity_complete;
                }
                break;
            }
            case -2086105689: {
                if(tag.equals("layout/fragment_about_fourth_0")) {
                    return youdrive.today.R.layout.fragment_about_fourth;
                }
                break;
            }
            case -1829977402: {
                if(tag.equals("layout/popup_close_car_0")) {
                    return youdrive.today.R.layout.popup_close_car;
                }
                break;
            }
            case -1446553997: {
                if(tag.equals("layout/item_profile_0")) {
                    return youdrive.today.R.layout.item_profile;
                }
                break;
            }
            case -872202657: {
                if(tag.equals("layout/marker_info_0")) {
                    return youdrive.today.R.layout.marker_info;
                }
                break;
            }
            case -1707787903: {
                if(tag.equals("layout/fragment_about_second_0")) {
                    return youdrive.today.R.layout.fragment_about_second;
                }
                break;
            }
            case -194099397: {
                if(tag.equals("layout/fragment_register_offert_0")) {
                    return youdrive.today.R.layout.fragment_register_offert;
                }
                break;
            }
            case 405033457: {
                if(tag.equals("layout/activity_confirmation_0")) {
                    return youdrive.today.R.layout.activity_confirmation;
                }
                break;
            }
            case -940201778: {
                if(tag.equals("layout/activity_wellcome_0")) {
                    return youdrive.today.R.layout.activity_wellcome;
                }
                break;
            }
            case -1904377723: {
                if(tag.equals("layout/fragment_about_first_0")) {
                    return youdrive.today.R.layout.fragment_about_first;
                }
                break;
            }
            case 326562930: {
                if(tag.equals("layout/item_phone_invite_0")) {
                    return youdrive.today.R.layout.item_phone_invite;
                }
                break;
            }
            case 446910321: {
                if(tag.equals("layout/activity_referal_0")) {
                    return youdrive.today.R.layout.activity_referal;
                }
                break;
            }
            case 1893753212: {
                if(tag.equals("layout/fragment_about_third_0")) {
                    return youdrive.today.R.layout.fragment_about_third;
                }
                break;
            }
            case 208534469: {
                if(tag.equals("layout/activity_thanks_0")) {
                    return youdrive.today.R.layout.activity_thanks;
                }
                break;
            }
            case -923250168: {
                if(tag.equals("layout/popup_open_car_0")) {
                    return youdrive.today.R.layout.popup_open_car;
                }
                break;
            }
            case -608183195: {
                if(tag.equals("layout/activity_inform_0")) {
                    return youdrive.today.R.layout.activity_inform;
                }
                break;
            }
            case 732737566: {
                if(tag.equals("layout/popup_distance_0")) {
                    return youdrive.today.R.layout.popup_distance;
                }
                break;
            }
            case 107535370: {
                if(tag.equals("layout/dialog_info_contents_0")) {
                    return youdrive.today.R.layout.dialog_info_contents;
                }
                break;
            }
            case 2115380023: {
                if(tag.equals("layout/fragment_register_documents_0")) {
                    return youdrive.today.R.layout.fragment_register_documents;
                }
                break;
            }
            case -225371335: {
                if(tag.equals("layout/activity_search_car_0")) {
                    return youdrive.today.R.layout.activity_search_car;
                }
                break;
            }
            case -1374787098: {
                if(tag.equals("layout/item_email_0")) {
                    return youdrive.today.R.layout.item_email;
                }
                break;
            }
            case 1185193333: {
                if(tag.equals("layout/activity_registration_0")) {
                    return youdrive.today.R.layout.activity_registration;
                }
                break;
            }
            case -237232145: {
                if(tag.equals("layout/activity_login_0")) {
                    return youdrive.today.R.layout.activity_login;
                }
                break;
            }
            case 579199021: {
                if(tag.equals("layout/header_profile_0")) {
                    return youdrive.today.R.layout.header_profile;
                }
                break;
            }
            case -1435807731: {
                if(tag.equals("layout/fragment_payment_0")) {
                    return youdrive.today.R.layout.fragment_payment;
                }
                break;
            }
            case -2119805979: {
                if(tag.equals("layout/item_image_0")) {
                    return youdrive.today.R.layout.item_image;
                }
                break;
            }
            case -155606011: {
                if(tag.equals("layout/activity_invite_0")) {
                    return youdrive.today.R.layout.activity_invite;
                }
                break;
            }
            case -130708746: {
                if(tag.equals("layout/item_popup_0")) {
                    return youdrive.today.R.layout.item_popup;
                }
                break;
            }
            case 2020401522: {
                if(tag.equals("layout/fragment_referal_0")) {
                    return youdrive.today.R.layout.fragment_referal;
                }
                break;
            }
            case 423966419: {
                if(tag.equals("layout/activity_maps_0")) {
                    return youdrive.today.R.layout.activity_maps;
                }
                break;
            }
            case 2074765033: {
                if(tag.equals("layout/activity_order_car_0")) {
                    return youdrive.today.R.layout.activity_order_car;
                }
                break;
            }
        }
        return 0;
    }
    String convertBrIdToString(int id) {
        if (id < 0 || id >= InnerBrLookup.sKeys.length) {
            return null;
        }
        return InnerBrLookup.sKeys[id];
    }
    private static class InnerBrLookup {
        static String[] sKeys = new String[]{
            "_all"
            ,"activity"
            ,"car"
            ,"emailContact"
            ,"listener"
            ,"phoneContact"};
    }
}
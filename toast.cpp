#include "toast.h"
//#include <QtAndroid>
//#include <QAndroidJniObject>
//#include <QDesktopServices>
//#include <QUrl>

Toast::Toast(QObject *parent) : QObject(parent)
{

}

void Toast::showToast(const QString &message, Toast::Duration duration) {
//    // all the magic must happen on Android UI thread
//    QtAndroid::runOnAndroidThread([message, duration] {
//        QAndroidJniObject javaString = QAndroidJniObject::fromString(message);
//        QAndroidJniObject toast = QAndroidJniObject::callStaticObjectMethod("android/widget/Toast", "makeText",
//                                                                            "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;",
//                                                                            QtAndroid::androidActivity().object(),
//                                                                            javaString.object(),
//                                                                            jint(duration));
//        toast.callMethod<void>("show");
//    });
}

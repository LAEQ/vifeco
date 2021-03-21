#
# spec file for package vifeco
#
# Copyright (c) 2021 SUSE LINUX GmbH, Nuernberg, Germany.
#
# All modifications and additions to the file contributed by third parties
# remain the property of their copyright owners, unless otherwise agreed
# upon. The license for this file, and modifications and additions to the
# file, is the same license as for the pristine package itself (unless the
# license for the pristine package is not an Open Source License, in which
# case the license is the MIT License). An "Open Source License" is a
# license that conforms to the Open Source Definition (Version 1.9)
# published by the Open Source Initiative.

# Please submit bugfixes or comments via http://bugs.opensuse.org/
#


Name:           vifeco
Version:        2.0
Release:        0
Summary:        Vifeco (video features counter) is a stand-alone application that makes it possible to manually identify features on a video.
# FIXME: Select a correct license from https://github.com/openSUSE/spec-cleaner#spdx-licenses
License:        Apache 2.0
Source:         %{name}.tar.gz
# FIXME: use correct group, see "https://en.opensuse.org/openSUSE:Package_group_guidelines"
Group:          Productivity/Other
Url:            https://github.com/LAEQ/vifeco
BuildRequires:  bash
BuildRoot:      %{_tmppath}/%{name}-%{version}-build

%description

%prep
# %setup -n %{name}

%build

%install
mkdir -p $RPM_BUILD_ROOT/usr/share/vifeco
install -m 700 vifeco/main $RPM_BUILD_ROOT/usr/share/vifeco

%post
%postun

%files
%defattr(-,root,root)
/usr/share/vifeco/main

%clean
rm -rf $RPM_BUILD_ROOT

# %doc ChangeLog README COPYING

%changelog
* Sun Mar 21 2021 David Maignan <davidmaignan@gmail.com>
- Original package. it is primarily intended to be test the process of building an RPM.
